package cn.chriswood.anthill.infrastructure.web.auth.exception

object InfrastructureWebAuthExceptionMessages {
    val messages = hashMapOf(
        "zh" to hashMapOf(
            "InfrastructureWebAuth.message" to "无效的鉴权，禁止访问",
            "InfrastructureWebAuth.noToken" to "没有鉴权信息",
            "InfrastructureWebAuth.invalidToken" to "无效的鉴权信息",
            "InfrastructureWebAuth.expireToken" to "鉴权信息已过期",
            "InfrastructureWebAuth.pushOffline" to "被顶下线",
            "InfrastructureWebAuth.kickOffline" to "被踢下线",
            "InfrastructureWebAuth.frozen" to "鉴权信息被冻结",
            "InfrastructureWebAuth.noCorrespondRole" to "没有对应的角色",
            "InfrastructureWebAuth.noCorrespondPermission" to "没有对应的权限",
        ),
        "en" to hashMapOf(
            "InfrastructureWebAuth.message" to "invalid token, forbid request",
            "InfrastructureWebAuth.noToken" to "no token",
            "InfrastructureWebAuth.invalidToken" to "invalid token",
            "InfrastructureWebAuth.expireToken" to "expire token",
            "InfrastructureWebAuth.pushOffline" to "you have being pushed offline",
            "InfrastructureWebAuth.kickOffline" to "you have being kicked offline",
            "InfrastructureWebAuth.frozen" to "you have being frozen",
            "InfrastructureWebAuth.noCorrespondRole" to "no correspond role",
            "InfrastructureWebAuth.noCorrespondPermission" to "no correspond permission",
        )
    )
}
