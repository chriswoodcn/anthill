package cn.chriswood.anthill.infrastructure.mybatisflex.support

import java.time.LocalDateTime

abstract class BaseTimeEntity : Create, Update {
    override var createTime: LocalDateTime? = null
    override var updateTime: LocalDateTime? = null
}
