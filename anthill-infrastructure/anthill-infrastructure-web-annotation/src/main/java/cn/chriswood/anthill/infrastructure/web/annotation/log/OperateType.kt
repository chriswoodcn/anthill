package cn.chriswood.anthill.infrastructure.web.annotation.log

enum class OperateType(val code: String) {
    /**
     * 其他
     */
    OTHER("0"),

    /**
     * 新增
     */
    INSERT("1"),

    /**
     * 修改
     */
    UPDATE("2"),

    /**
     * 删除
     */
    DELETE("3"),

    /**
     * 导出
     */
    EXPORT("4"),

    /**
     * 导入
     */
    IMPORT("5"),

    /**
     * 强退
     */
    FORCE("6"),

    /**
     * 清空数据
     */
    CLEAN("7"),
}
