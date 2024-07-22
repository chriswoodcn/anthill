package com.taotao.bmm.support

/**
 * 常量
 */
object Constants {
    /**
     * 密码正则表达式
     */
    const val REGEX_BACKEND_PASSWORD = "^[\\w~!@#\$%^*()\\-+]{6,18}\$"

    /**
     * 管理员权限
     */
    const val PERMISSION_ADMIN = "*:*:*"

    /**
     * 正常
     */
    const val STATUS_0 = "0"

    /**
     * 停用
     */
    const val STATUS_1 = "1"

    /**
     * 逻辑删除
     */
    const val STATUS_2 = "2"

    /**
     * 系统数据
     */
    const val STATUS_3 = "3"

    /**
     * 否
     */
    const val FLAG_NOT = "0"

    /**
     * 是
     */
    const val FLAG_IS = "1"

    /**
     * 其他
     */
    const val BIZ_TYPE_0 = "0"

    /**
     * 增
     */
    const val BIZ_TYPE_1 = "1"

    /**
     * 改
     */
    const val BIZ_TYPE_2 = "2"

    /**
     * 删
     */
    const val BIZ_TYPE_3 = "3"

    /**
     * 超管 所属
     */
    const val AFFILIATE_FLAG_0 = "0"

    /**
     * 系统用户 所属
     */
    const val AFFILIATE_FLAG_1 = "1"

    /**
     * 客户 所属
     */
    const val AFFILIATE_FLAG_2 = "2"

    /**
     * 模板
     */
    const val AFFILIATE_FLAG_T = "T"

    /**
     * 其他
     */
    const val USER_TYPE_0 = "0"

    /**
     * 系统用户
     */
    const val USER_TYPE_1 = "1"

    /**
     * APP用户
     */
    const val USER_TYPE_2 = "2"

    /**
     * 接口缓存
     */
    const val CACHE_GET_ROUTERS = "Cacheable:SysUserController_getRouters#10m#10m#20"
    const val CACHE_MENU_SELECT = "Cacheable:SysMenuController_menuSelect#10m#10m#20"

    /**
     * 系统缓存
     */
    const val CACHE_CONFIG = "Cache_Sys_Config"
    const val CACHE_DICT = "Cache_Sys_Dict"
}
