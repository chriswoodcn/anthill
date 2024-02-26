package cn.chriswood.anthill.infrastructure.mybatisflex.support

import java.time.LocalDateTime

abstract class BaseTimeLogicEntity : Create, Update, LogicDelete {
    override var createTime: LocalDateTime? = null
    override var updateTime: LocalDateTime? = null
    override var delFlag: String = "0"
}
