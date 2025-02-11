package cn.chriswood.anthill.example.mybatisflex.dto

import cn.chriswood.anthill.example.flex.support.FlexQueryColumn
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import com.mybatisflex.annotation.Table
import com.mybatisflex.core.constant.SqlOperator
import java.time.LocalDateTime

@NoArgs
@Table("t_user", dataSource = "ds1")
data class TUserDs1Dto(
    @FlexQueryColumn
    var userId: Long = 0L,

    @FlexQueryColumn(SqlOperator.LIKE)
    var username: String = StringUtil.EMPTY,

    @FlexQueryColumn
    var password: String? = null,

    var createTime: LocalDateTime? = null
)

