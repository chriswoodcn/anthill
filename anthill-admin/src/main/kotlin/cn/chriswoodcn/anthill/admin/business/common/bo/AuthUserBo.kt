package com.taotao.bmm.business.common.bo

import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthUser

@NoArgs
class AuthUserBo<T>(
    override var instance: T,
    override var userId: String,
    override var userType: String,
    override var username: String?,
    override var permissions: Set<String>,
    override var roles: Set<String>,
) : AuthUser<T>