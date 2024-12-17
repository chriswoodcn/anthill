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

    private val mailAccounts: MutableMap<String, EnhanceMailAccount> = mutableMapOf()

    private var hasFetchBeans = false

    private var availableEnhanceMailAccount = ThreadLocal<EnhanceMailAccount>()

    init {
        init()
    }

    private fun init() {
        val beansOfType = SpringUtil.getBeansOfType(EnhanceMailAccount::class.java)
        addMailAccounts(beansOfType.values)
        hasFetchBeans = true
        log.debug(">>>>>>>>>> MailPool fetch enhanceMailAccount size:${mailAccounts.size}>>>>>>>>>>")
    }

    fun addMailAccount(account: EnhanceMailAccount) {
        mailAccounts[account.mailAccount.from] = account
    }

    fun addMailAccounts(accounts: Collection<EnhanceMailAccount>) {
        accounts.forEach {
            mailAccounts[it.mailAccount.from] = it
        }
    }

    fun getAvailableMailAccount(key: String?): MailPool {
        if (!hasFetchBeans && mailAccounts.isEmpty()) {
            init()
        }
        log.debug(
            "[MailPool] [getOneAvailableMailAccount] mailAccounts.size: {}, mailAccounts.keys: {}",
            mailAccounts.size,
            mailAccounts.keys
        )
        if (mailAccounts.isEmpty()) {
            throw MailException("mail account pool is empty")
        }
        if (!key.isNullOrBlank()) {
            val account = mailAccounts[key]
            if (account != null) {
                this.availableEnhanceMailAccount.set(account)
                return this
            }
        }
        val filtered = mailAccounts.values.filter {
            !(it.sendCount >= it.limitCount && it.limitCount != -1)
        }
        val sorted = filtered.sortedBy { it.sendCount }
        if (sorted.isEmpty()) {
            throw MailException("no available mail account")
        }
        log.debug("[MailPool] [getOneAvailableMailAccount] use account: ${sorted[0].mailAccount.user}")
        this.availableEnhanceMailAccount.set(sorted[0])
        return this
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
            enhanceMailAccount.sendCount += 1
        } catch (e: Exception) {
            log.error("[MailPool] [sendAll] error: {}", e.message)
        } finally {
            // 如果现在比较上次发送日期已经过去了 重置sendCount为1
            val lastSendDate = enhanceMailAccount.lastSendTime
            val nowDate = LocalDate.now()
            enhanceMailAccount.lastSendTime = nowDate
            if (nowDate.isAfter(lastSendDate)) {
                enhanceMailAccount.sendCount = 1
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
