package cn.chriswood.anthill.reactive.example.modules.vo

import cn.chriswood.anthill.infrastructure.json.annotation.Sensitive
import java.time.LocalDateTime

class SysUserVo {
    var userId: Long = 0
    var username: String? = null

    @Sensitive(role = "*", perm = "*")
    var password: String? = null
    var salt: String? = null
    var email: String? = null
    var mobile: String? = null
    var status: String? = null
    var createUserId: Long? = null
    var createTime: LocalDateTime? = null
}
