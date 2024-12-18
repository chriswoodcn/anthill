package cn.chriswood.anthill.infrastructure.web.auth.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.web.auth")
data class AuthProperties(
    /**
     * 开关
     */
    val enabled: Boolean?,

    /**
     * 系统用户登录检查路径
     */
    val sysUserCheckPath: String = "/backend/**",

    /**
     * APP用户登录检查路径
     */
    val appUserCheckPath: String = "/app/**",

    /**
     * 需要排除鉴权的路径
     */
    val excludes: List<String?> = emptyList(),
)
