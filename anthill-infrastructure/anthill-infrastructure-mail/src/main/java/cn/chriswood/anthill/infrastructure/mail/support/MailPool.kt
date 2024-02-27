package cn.chriswood.anthill.infrastructure.mail.support

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.map.MapUtil
import cn.hutool.core.util.CharUtil
import cn.hutool.core.util.StrUtil
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.time.LocalDate

object MailPool {
    private val log = LoggerFactory.getLogger(javaClass)

    private val mailAccounts: List<EnhanceMailAccount> = mutableListOf()

    fun addMailAccount(account: EnhanceMailAccount) {
        mailAccounts.plus(account)
    }

    fun addMailAccounts(accounts: List<EnhanceMailAccount>) {
        mailAccounts.plus(accounts)
    }

    fun getOneAvailableMailAccount(): EnhanceMailAccount? {
        val filtered = mailAccounts.filter {
            !(it.sendCount >= it.limitCount && it.limitCount != -1)
        }
        val sorted = filtered.sortedBy { it.sendCount }
        if (sorted.isEmpty()) return null
        return sorted[0]
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

    fun sendTo(to: String, subject: String, content: String, isHtml: Boolean, vararg files: File?): String? {
        return sendTos(splitAddress(to), subject, content, isHtml, *files)
    }

    fun sendTos(
        tos: Collection<String>,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File?
    ): String? {
        return sendAll(
            tos,
            null,
            null,
            subject,
            content,
            isHtml,
            *files
        )
    }

    fun sendAll(
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File?
    ): String? {
        val oneAvailableMailAccount = getOneAvailableMailAccount()
        if (oneAvailableMailAccount == null) {
            log.error("[MailPool] [sendAll] error: oneAvailableMailAccount is null")
            return null
        }
        return doSend(
            oneAvailableMailAccount,
            false,
            tos,
            ccs,
            bccs,
            subject,
            content,
            null,
            isHtml,
            *files
        )
    }

    fun doSend(
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
        if (enhanceMailAccount.mailAccount == null) {
            log.error("[MailPool] [doSend] error: enhanceMailAccount.mailAccount is null")
            return null
        }
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
        enhanceMailAccount.sendCount += 1
        val lastSendDate = enhanceMailAccount.lastSendTime
        val nowDate = LocalDate.now()
        enhanceMailAccount.lastSendTime = nowDate
        // 如果现在比较上次发送日期已经过去了 重置sendCount为1
        if (nowDate.isAfter(lastSendDate)) {
            enhanceMailAccount.sendCount = 1
        }
        return mail.send()
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
