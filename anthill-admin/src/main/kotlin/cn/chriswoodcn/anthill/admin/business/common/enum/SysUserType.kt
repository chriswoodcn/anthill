package com.taotao.bmm.business.common.enum

enum class SysUserType(val code: String) {
    SUPER_USER("0"),
    MAINTAIN_USER("1"),
    SYS_USER("2")
}

fun getSysUserType(code: String): SysUserType {
    return SysUserType.entries.find { it.code == code } ?: SysUserType.SYS_USER
}