package cn.chriswood.anthill.infrastructure.mybatisflex.support

abstract class BaseTimeByEntity : BaseTimeEntity(), CreateBy, UpdateBy {
    override var createBy: Long? = null
    override var updateBy: Long? = null
}
