package cn.chriswood.anthill.example.jpa.dynamic.persistence.entity

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "t_user")
data class TUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var userId: Long = 0L,

    @Column(name = "username")
    var username: String = StringUtil.EMPTY,

    @Column(name = "password")
    var password: String? = null,

    @Column(name = "create_time")
    var createTime: LocalDateTime? = null
)
