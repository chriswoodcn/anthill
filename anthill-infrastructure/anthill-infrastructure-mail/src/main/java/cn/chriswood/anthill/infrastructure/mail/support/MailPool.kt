package cn.chriswood.anthill.infrastructure.mail.support

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.map.MapUtil
import cn.hutool.core.util.CharUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * MailPool
 *
 * usage:
 *
 * MailPool.getOneAvailableMailAccount().sendText(to, subject, content)
 *
 */
object MailPool {

    private val log = LoggerFactory.getLogger(javaClass)

    private val mailAccounts: ConcurrentMap<String, MutableList<EnhanceMailAccount>> = ConcurrentHashMap()

    private var hasFetchBeans = false

    private var availableEnhanceMailAccount = ThreadLocal<EnhanceMailAccount>()

    init {
        init()
    }

    private fun init() {
        val beans = SpringUtil.getBeansOfType(EnhanceMailAccount::class.java)
        beans.forEach {
            addMailAccount(it.value.key, it.value)
        }
        hasFetchBeans = true
        mailAccounts.forEach { (k, v) ->
            log.debug(">>>>>>>>>> MailPool fetch enhanceMailAccount key = $k , size = ${v.size} >>>>>>>>>>")
        }

    }

    fun addMailAccount(key: String, account: EnhanceMailAccount) {
        var list = mailAccounts[key]
        if (null == list) {
            mailAccounts[key] = mutableListOf()
            list = mailAccounts[key]
        }
        list!!.add(account)
    }

    fun addMailAccounts(key: String, accounts: Collection<EnhanceMailAccount>) {
        var list = mailAccounts[key]
        if (null == list) {
            mailAccounts[key] = mutableListOf()
            list = mailAccounts[key]
        }
        list!!.addAll(accounts)
    }

    fun getAvailableMailAccount(key: String?, default: String): MailPool {
        if (!hasFetchBeans && mailAccounts.isEmpty()) {
            init()
        }
        if (mailAccounts.isEmpty()) {
            throw MailException("Mail Pool is empty")
        }
        val k = key ?: default

        val account = mailAccounts[k]
        if (!account.isNullOrEmpty()) {
            account.filter {
                it.dayCount < it.limitDayCount && it.hourCount < it.limitHourCount
            }.sortedBy { it.hourCount }.sortedBy { it.dayCount }
            if (account.isEmpty()) {
                throw MailException("No Available Mail Account")
            }
            this.availableEnhanceMailAccount.set(account[0])
            return this
        } else {
            throw MailException("Mail Accounts is empty")
        }
    }

    fun sendText(to: String, subject: String, content: String, vararg files: File?): Boolean {
        val res = sendTo(to, subject, content, false, *files) ?: return false
        log.debug("[MailPool] [sendText] res: {}", res)
        return true
    }

    fun sendHtml(to: String, subject: String, content: String, vararg files: File?): Boolean {
        val res = sendTo(to, subject, content, false, *files) ?: return false
        log.debug("[MailPool] [sendText] res: {}", res)
        return true
    }

    private fun sendTo(to: String, subject: String, content: String, isHtml: Boolean, vararg files: File?): String? {
        return sendTos(splitAddress(to), subject, content, isHtml, *files)
    }

    private fun sendTos(
        tos: Collection<String>, subject: String, content: String, isHtml: Boolean, vararg files: File?
    ): String? {
        return sendAll(
            tos, null, null, subject, content, isHtml, *files
        )
    }

    private fun sendAll(
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File?
    ): String? {
        if (availableEnhanceMailAccount.get() == null) {
            log.error("[MailPool] [sendAll] error: available mail account is null")
            return null
        }
        return doSend(
            availableEnhanceMailAccount.get()!!, false, tos, ccs, bccs, subject, content, null, isHtml, *files
        )
    }

    private fun doSend(
        enhanceMailAccount: EnhanceMailAccount,
        useGlobalSession: Boolean,
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File?
    ): String? {
        val mail = Mail.create(enhanceMailAccount.mailAccount).setUseGlobalSession(useGlobalSession)

        // 可选抄送人
        if (CollUtil.isNotEmpty(ccs) && ccs != null) {
            mail.setCcs(*ccs.toTypedArray<String?>())
        }
        // 可选密送人
        if (CollUtil.isNotEmpty(bccs) && bccs != null) {
            mail.setBccs(*bccs.toTypedArray<String?>())
        }
        mail.setTos(*tos.toTypedArray<String>())
        mail.setTitle(subject)
        mail.setContent(content)
        mail.setHtml(isHtml)
        mail.setFiles(*files)

        // 图片
        if (MapUtil.isNotEmpty(imageMap) && imageMap != null) {
            for ((key, value) in imageMap) {
                mail.addImage(key, value)
                // 关闭流
                IoUtil.close(value)
            }
        }
        var res: String? = null
        try {
            res = mail.send()
            //发送成功 计数加1
            enhanceMailAccount.dayCount += 1
            enhanceMailAccount.hourCount += 1
        } catch (e: Exception) {
            log.error("[MailPool] [doSend] error: {}", e.message)
        } finally {
            if (enhanceMailAccount.limitDayCount != Int.MAX_VALUE) {
                // 如果现在比较上次发送日期已经过去了 重置dayCount为1
                val lastSendDate = enhanceMailAccount.lastSendDate
                val nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern(Constants.PATTERN_YYYY_MM_DD))
                enhanceMailAccount.lastSendDate = nowDate
                if (nowDate != lastSendDate) {
                    enhanceMailAccount.dayCount = 1
                }
            }
            if (enhanceMailAccount.limitHourCount != Int.MAX_VALUE) {
                val lastSendHour = enhanceMailAccount.lastSendHour
                val nowDateTime =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.PATTERN_YYYY_MM_DD_HH))
                enhanceMailAccount.lastSendHour = nowDateTime
                if (lastSendHour != nowDateTime) {
                    enhanceMailAccount.hourCount = 1
                }
            }
            //可用邮箱账号重置
            availableEnhanceMailAccount.set(null)
        }
        return res
    }

    private fun splitAddress(addresses: String): List<String> {
        val result: List<String> = if (StrUtil.contains(addresses, CharUtil.COMMA)) {
            StrUtil.splitTrim(addresses, CharUtil.COMMA)
        } else if (StrUtil.contains(addresses, ';')) {
            StrUtil.splitTrim(addresses, ';')
        } else {
            CollUtil.newArrayList(addresses)
        }
        return result
    }
}
