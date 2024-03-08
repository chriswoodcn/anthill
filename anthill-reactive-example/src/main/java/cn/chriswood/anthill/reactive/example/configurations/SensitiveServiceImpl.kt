package cn.chriswood.anthill.reactive.example.configurations

import cn.chriswood.anthill.infrastructure.json.support.SensitiveService
import org.springframework.stereotype.Component

@Component
class SensitiveServiceImpl : SensitiveService {
    override fun isSensitive(role: String, perms: String): Boolean {
        return true
    }
}
