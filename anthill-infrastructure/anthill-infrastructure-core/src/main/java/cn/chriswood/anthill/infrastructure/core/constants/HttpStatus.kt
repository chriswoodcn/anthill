package cn.chriswood.anthill.infrastructure.core.constants

object HttpStatus {
    val SUCCESS: Int = 200

    /**
     * 对象创建成功
     */
    val CREATED: Int = 201

    /**
     * 请求已经被接受
     */
    val ACCEPTED: Int = 202

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    val NO_CONTENT: Int = 204

    /**
     * 资源已被移除
     */
    val MOVED_PERM: Int = 301

    /**
     * 重定向
     */
    val SEE_OTHER: Int = 303

    /**
     * 资源没有被修改
     */
    val NOT_MODIFIED: Int = 304

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    val BAD_REQUEST: Int = 400

    /**
     * 未授权
     */
    val UNAUTHORIZED: Int = 401

    /**
     * 访问受限，授权过期
     */
    val FORBIDDEN: Int = 403

    /**
     * 资源，服务未找到
     */
    val NOT_FOUND: Int = 404

    /**
     * 不允许的http方法
     */
    val BAD_METHOD: Int = 405

    /**
     * 资源冲突，或者资源被锁
     */
    val CONFLICT: Int = 409

    /**
     * 不支持的数据，媒体类型
     */
    val UNSUPPORTED_TYPE: Int = 415

    /**
     * 系统内部错误
     */
    val FAIL: Int = 500

    /**
     * 接口未实现
     */
    val NOT_IMPLEMENTED: Int = 501

    /**
     * 系统警告消息
     */
    val WARN: Int = 601
}
