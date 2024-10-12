import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.stp.StpLogic
import cn.dev33.satoken.stp.StpUtil


/**
 * StpLogic 门面类，管理项目中所有的 StpLogic 账号体系
 */
object StpKit {
    
    val Default = StpUtil.stpLogic

    val SysUser = StpLogic(UserType.SYS_USER.code)

    val AppUser = StpLogic(UserType.APP_USER.code)
}
