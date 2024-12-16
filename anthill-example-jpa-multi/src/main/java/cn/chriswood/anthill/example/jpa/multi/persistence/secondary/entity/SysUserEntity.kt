package cn.chriswood.anthill.example.jpa.multi.persistence.secondary.entity

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sys_user")
data class SysUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var userId: Long = 0L,

    @Column(name = "username")
    var username: String = StringUtil.EMPTY,

    @Column(name = "create_time")
    var createTime: LocalDateTime? = null
)
