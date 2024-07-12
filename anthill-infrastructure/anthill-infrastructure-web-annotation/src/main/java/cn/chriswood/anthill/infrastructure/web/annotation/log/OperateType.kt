package cn.chriswood.anthill.infrastructure.web.annotation.log

enum class OperateType(code: String) {
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
     * 授权
     */
    GRANT("4"),

    /**
     * 导出
     */
    EXPORT("5"),

    /**
     * 导入
     */
    IMPORT("6"),

    /**
     * 强退
     */
    FORCE("7"),

    /**
     * 生成代码
     */
    GENCODE("8"),

    /**
     * 清空数据
     */
    CLEAN("9"),
}
