package cn.chriswood.anthill.example.mybatisflex.dto

import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table
import java.time.LocalDateTime

@NoArgs
@Table("t_user", dataSource = "ds2")
data class TUserDs2Dto(
    @Id(keyType = KeyType.Auto)
    var userId: Long = 0L,

    @Column("username")
    var username: String = StringUtil.EMPTY,

    @Column("password")
    var password: String? = null,

    @Column("create_time")
    var createTime: LocalDateTime? = null
)

