package cn.chriswood.anthill.infrastructure.mail.support

import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.setting.Setting
import java.io.Serial
import java.io.Serializable
import java.nio.charset.Charset
import java.util.*

class MailAccount : Serializable {
    companion object {
        @Serial
        private val serialVersionUID = -6937313421815719204L
        private const val MAIL_PROTOCOL = "mail.transport.protocol"
        private const val SMTP_HOST = "mail.smtp.host"
        private const val SMTP_PORT = "mail.smtp.port"
        private const val SMTP_AUTH = "mail.smtp.auth"
        private const val SMTP_TIMEOUT = "mail.smtp.timeout"
        private const val SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout"
        private const val SMTP_WRITE_TIMEOUT = "mail.smtp.writetimeout"

        // SSL
        private const val STARTTLS_ENABLE = "mail.smtp.starttls.enable"
        private const val SSL_ENABLE = "mail.smtp.ssl.enable"
        private const val SSL_PROTOCOLS = "mail.smtp.ssl.protocols"
        private const val SOCKET_FACTORY = "mail.smtp.socketFactory.class"
        private const val SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback"
        private const val SOCKET_FACTORY_PORT = "smtp.socketFactory.port"

        // System Properties
        private const val SPLIT_LONG_PARAMS = "mail.mime.splitlongparameters"

        //private static final String ENCODE_FILE_NAME = "mail.mime.encodefilename";
        //private static final String CHARSET = "mail.mime.charset";
        // 其他
        private const val MAIL_DEBUG = "mail.debug"
        val MAIL_SETTING_PATHS = arrayOf("config/mail.setting", "config/mailAccount.setting", "mail.setting")
    }

    /**
     * SMTP服务器域名
     */
    var host: String? = null

    fun setHost(host: String): MailAccount {
        this.host = host
        return this
    }

    /**
     * SMTP服务端口
     */
    var port: Int? = null

    fun setPort(port: Int): MailAccount {
        this.port = port
        return this
    }

    /**
     * 是否需要用户名密码验证
     */
    var auth = false

    fun setAuth(auth: Boolean): MailAccount {
        this.auth = auth
        return this
    }

    /**
     * 用户名
     */
    var user: String? = null

    fun setUser(user: String): MailAccount {
        this.user = user
        return this
    }

    /**
     * 密码
     */
    var pass: String? = null

    fun setPass(pass: String): MailAccount {
        this.pass = pass
        return this
    }

    /**
     * 发送方，遵循RFC-822标准
     */
    var from: String? = null

    fun setFrom(from: String): MailAccount {
        this.from = from
        return this
    }

    /**
     * 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
     */
    var debug = false

    fun setDebug(debug: Boolean): MailAccount {
        this.debug = debug
        return this
    }

    /**
     * 编码用于编码邮件正文和发送人、收件人等中文
     */
    var charset: Charset = CharsetUtil.CHARSET_UTF_8

    fun setCharset(charset: Charset): MailAccount {
        this.charset = charset
        return this
    }

    /**
     * 对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
     */
    var splitlongparameters = false

    fun setSplitlongparameters(splitlongparameters: Boolean): MailAccount {
        this.splitlongparameters = splitlongparameters
        return this
    }

    /**
     * 对于文件名是否使用{@link #charset}编码，默认为 {@code true}
     */
    var encodefilename = true

    fun setEncodefilename(encodefilename: Boolean): MailAccount {
        this.encodefilename = encodefilename
        return this
    }

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     */
    var starttlsEnable = false

    fun setStarttlsEnable(startttlsEnable: Boolean): MailAccount {
        this.starttlsEnable = startttlsEnable
        return this
    }

    /**
     * 使用 SSL安全连接
     */
    var sslEnable = true

    fun setSslEnable(sslEnable: Boolean): MailAccount {
        this.sslEnable = sslEnable
        return this
    }

    /**
     * SSL协议，多个协议用空格分隔
     */
    var sslProtocols: String? = null

    fun setSslProtocols(sslProtocols: String): MailAccount {
        this.sslProtocols = sslProtocols
        return this
    }

    /**
     * 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
     */
    var socketFactoryClass = "javax.net.ssl.SSLSocketFactory"

    fun setSocketFactoryClass(socketFactoryClass: String): MailAccount {
        this.socketFactoryClass = socketFactoryClass
        return this
    }

    /**
     * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
     */
    var socketFactoryFallback = false

    fun setSocketFactoryFallback(socketFactoryFallback: Boolean): MailAccount {
        this.socketFactoryFallback = socketFactoryFallback
        return this
    }

    /**
     * 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
     */
    var socketFactoryPort = 465

    fun setSocketFactoryPort(socketFactoryPort: Int): MailAccount {
        this.socketFactoryPort = socketFactoryPort
        return this
    }

    /**
     * SMTP超时时长，单位毫秒，缺省值不超时
     */
    var timeout: Long = 0

    fun setTimeout(timeout: Long): MailAccount {
        this.timeout = timeout
        return this
    }

    /**
     * Socket连接超时值，单位毫秒，缺省值不超时
     */
    var connectionTimeout: Long = 0

    fun setConnectionTimeout(connectionTimeout: Long): MailAccount {
        this.connectionTimeout = connectionTimeout
        return this
    }

    /**
     * Socket写出超时值，单位毫秒，缺省值不超时
     */
    var writeTimeout: Long = 0

    fun setWriteTimeout(writeTimeout: Long): MailAccount {
        this.writeTimeout = writeTimeout
        return this
    }

    /**
     * 自定义的其他属性，此自定义属性会覆盖默认属性
     */
    val customProperty: Map<String, Any> = HashMap()

    fun setCustomProperty(key: String, value: Any): MailAccount {
        if (key.isNotBlank() && ObjectUtil.isNotNull(value)) {
            customProperty.plus(key to value)
        }
        return this
    }

    /**
     * 构造,所有参数需自行定义或保持默认值
     */
    constructor()

    /**
     * 构造
     *
     * @param settingPath 配置文件路径
     */
    constructor(settingPath: String?) : this(Setting(settingPath))

    /**
     * 构造
     *
     * @param setting 配置文件
     */
    constructor(setting: Setting) {
        setting.toBean(this)
    }

    /**
     * 获得SMTP相关信息
     *
     * @return [Properties]
     */
    fun getSmtpProps(): Properties {
        //全局系统参数
        System.setProperty(SPLIT_LONG_PARAMS, splitlongparameters.toString())
        val p = Properties()
        p[MAIL_PROTOCOL] = "smtp"
        p[SMTP_HOST] = host
        p[SMTP_PORT] = port.toString()
        p[SMTP_AUTH] = auth.toString()
        if (timeout > 0) {
            p[SMTP_TIMEOUT] = timeout.toString()
        }
        if (connectionTimeout > 0) {
            p[SMTP_CONNECTION_TIMEOUT] = connectionTimeout.toString()
        }
        // issue#2355
        if (writeTimeout > 0) {
            p[SMTP_WRITE_TIMEOUT] = writeTimeout.toString()
        }
        p[MAIL_DEBUG] = debug.toString()
        if (starttlsEnable) {
            //STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
            p[STARTTLS_ENABLE] = "true"
        }

        // SSL
        if (sslEnable) {
            p[SSL_ENABLE] = "true"
            p[SOCKET_FACTORY] = socketFactoryClass
            p[SOCKET_FACTORY_FALLBACK] = socketFactoryFallback.toString()
            p[SOCKET_FACTORY_PORT] = socketFactoryPort.toString()
            // issue#IZN95@Gitee，在Linux下需自定义SSL协议版本
            if (StrUtil.isNotBlank(sslProtocols)) {
                p[SSL_PROTOCOLS] = sslProtocols
            }
        }

        // 补充自定义属性，允许自定属性覆盖已经设置的值
        p.putAll(customProperty)
        return p
    }

    /**
     * 如果某些值为null，使用默认值
     *
     * @return this
     */
    fun defaultIfEmpty(): MailAccount {
        // 去掉发件人的姓名部分
        val fromAddress = InternalMailUtil.parseFirstAddress(from, charset).address
        if (StrUtil.isBlank(host)) {
            // 如果SMTP地址为空，默认使用smtp.<发件人邮箱后缀>
            host = StrUtil.format("smtp.{}", StrUtil.subSuf(fromAddress, fromAddress.indexOf('@') + 1))
        }
        if (StrUtil.isBlank(user)) {
            // 如果用户名为空，默认为发件人（issue#I4FYVY@Gitee）
            //this.user = StrUtil.subPre(fromAddress, fromAddress.indexOf('@'));
            user = fromAddress
        }
        if (null == port) {
            // 端口在SSL状态下默认与socketFactoryPort一致，非SSL状态下默认为25
            port = if (sslEnable) socketFactoryPort else 25
        }
        return this
    }

    override fun toString(): String {
        return ("""
            MailAccount
            [host=$host,
            ort=$port,
            auth=$auth,
            user=$user,
            pass=${if (StrUtil.isEmpty(pass)) "" else "******"},
            from=$from,
            startttlsEnable=$starttlsEnable,
            socketFactoryClass=$socketFactoryClass,
            socketFactoryFallback=$socketFactoryFallback,
            socketFactoryPort=$socketFactoryPort]
            """)
    }
}

