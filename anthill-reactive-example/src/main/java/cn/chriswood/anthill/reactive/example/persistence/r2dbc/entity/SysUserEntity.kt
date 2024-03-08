package cn.chriswood.anthill.reactive.example.persistence.r2dbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sys_user")
data class SysUserEntity(
    @Id
    val userId: Long = 0L,
    val username: String? = null,
    val password: String? = null,
    val salt: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val status: String? = null,
    val createUserId: Long? = null,
    val createTime: LocalDateTime? = null,
)
