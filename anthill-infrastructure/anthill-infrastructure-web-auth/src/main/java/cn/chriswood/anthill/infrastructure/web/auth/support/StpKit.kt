
import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpUtil


/**
 * StpLogic 门面类，管理项目中所有的 StpLogic 账号体系
 */
object StpKit {

    val Default = StpUtil.stpLogic

    val SysUser = StpLogicJwtForSimple(UserType.SYS_USER.code)

    val AppUser = StpLogicJwtForSimple(UserType.APP_USER.code)
}
