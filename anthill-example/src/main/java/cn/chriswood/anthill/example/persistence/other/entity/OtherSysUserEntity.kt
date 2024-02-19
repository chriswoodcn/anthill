package cn.chriswood.anthill.example.persistence.other.entity

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sys_user")
class OtherSysUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var userId: Long = 0L
    @Column(name = "user_name")
    var userName: String = StringUtil.EMPTY
//    var password: String = StringUtil.EMPTY
//    var salt: String = StringUtil.EMPTY
//    var email: String? = null
//    var mobile: String? = null
//    var status: String? = null
//    var createUserId: Long? = null
    @Column(name = "create_time")
    var createTime: LocalDateTime? = null
}
