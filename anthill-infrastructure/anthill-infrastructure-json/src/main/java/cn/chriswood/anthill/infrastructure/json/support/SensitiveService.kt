package cn.chriswood.anthill.infrastructure.json.support

/**
 * 脱敏服务
 */
interface SensitiveService {
    fun isSensitive(role: String, perms: String): Boolean
}
