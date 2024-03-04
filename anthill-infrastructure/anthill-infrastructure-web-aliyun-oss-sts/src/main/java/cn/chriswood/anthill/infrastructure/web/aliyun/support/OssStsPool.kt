package cn.chriswood.anthill.infrastructure.web.aliyun.support

import cn.chriswood.anthill.infrastructure.core.config.ApplicationConfig
import cn.hutool.core.net.NetUtil
import cn.hutool.extra.spring.SpringUtil
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.IAcsClient
import com.aliyuncs.auth.sts.AssumeRoleRequest
import com.aliyuncs.auth.sts.AssumeRoleResponse
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.profile.DefaultProfile
import org.slf4j.LoggerFactory

/**
 *  aliyun oss sts配置池
 */
object OssStsPool {

    private val log = LoggerFactory.getLogger(javaClass)

    private val DEFAULT_KEY = "primary"

    private var hasFetchBeans = false

    private val ossStsDefinitions: MutableMap<String, OssStsProperty> = mutableMapOf()

    init {
        init()
    }

    private fun init() {
        val ossStsProperties = SpringUtil.getBean(OssStsProperties::class.java) ?: null
        if (null != ossStsProperties && !ossStsProperties.oss.isNullOrEmpty()) {
            ossStsDefinitions.plusAssign(ossStsProperties.oss)
        }
        hasFetchBeans = true
        log.debug(">>>>>>>>>> OssStsPool fetch ossStsProperties size:${ossStsProperties?.oss?.size}>>>>>>>>>>")
    }

    fun addOssStsDefinition(key: String, prop: OssStsProperty) {
        ossStsDefinitions + (key to prop)
    }

    fun addOssStsDefinition(pair: Pair<String, OssStsProperty>) {
        ossStsDefinitions + pair
    }

    fun addOssStsDefinitions(pairs: Iterable<Pair<String, OssStsProperty>>) {
        ossStsDefinitions.plus(pairs)
    }

    private fun getAvailableOssStsProperty(key: String): OssStsProperty? {
        if (!hasFetchBeans && ossStsDefinitions.isEmpty()) {
            init()
        }
        if (ossStsDefinitions.isEmpty()) {
            log.error("[OssStsPool] ossStsDefinitions is empty")
            return null
        }
        var ossStsProperty: OssStsProperty? = ossStsDefinitions[key]
        if (null == ossStsProperty) log.warn("[OssStsPool] ossStsDefinitions.$key is empty")
        ossStsProperty = ossStsDefinitions[DEFAULT_KEY]
        if (null == ossStsProperty) log.warn("[OssStsPool] ossStsDefinitions.$DEFAULT_KEY is empty")
        return ossStsProperty
    }

    fun getSts(key: String): AssumeRoleResponse? {
        val stsProperty = getAvailableOssStsProperty(key) ?: return null
        //构建一个阿里云客户端，用于发起请求。
        //构建阿里云客户端时需要设置AccessKey ID和AccessKey Secret。
        val profile: DefaultProfile = DefaultProfile
            .getProfile(
                stsProperty.regionId,
                stsProperty.accessKeyId,
                stsProperty.accessKeySecret
            )
        val client: IAcsClient = DefaultAcsClient(profile)
        //构造请求，设置参数。关于参数含义和设置方法，请参见《API参考》。
        val request = AssumeRoleRequest()
        request.sysRegionId = stsProperty.regionId
        request.roleArn = stsProperty.roleArn
        val config = SpringUtil.getBean(ApplicationConfig::class.java) ?: null
        request.roleSessionName = if (null == config) {
            "${stsProperty.roleSessionName}-${NetUtil.getLocalhostStr()}"
        } else "${config.name}-${stsProperty.roleSessionName}-${NetUtil.getLocalhostStr()}"
        //发起请求，并得到响应。
        try {
            return client.getAcsResponse(request)
        } catch (e: ClientException) {
            log.error("[OssStsPool] getSts error: {} {}", e.errCode, e.errMsg)
        }
        return null
    }

}
