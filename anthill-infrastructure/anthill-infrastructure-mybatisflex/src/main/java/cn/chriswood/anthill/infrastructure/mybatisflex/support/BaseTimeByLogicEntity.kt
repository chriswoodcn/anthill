package cn.chriswood.anthill.infrastructure.mybatisflex.support

abstract class BaseTimeByLogicEntity : BaseTimeEntity(), CreateBy, UpdateBy, LogicDelete {
    override var createBy: Long? = null
    override var updateBy: Long? = null
    override var delFlag: String = "0"
}
