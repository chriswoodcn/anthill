package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.*

@EntityListeners(JpaEntityListener::class)
@Entity
@Table(name = "sys_login_info")
data class SysLoginInfoEntity(
    @Id
    @GeneratedValue
    val id: Long?,
    val account: String?,
    val ipaddr: String?,
    val loginLocation: String?,
    val browser: String?,
    val os: String?,
    val status: String?,
    val msg: String?,
    val loginTime: Long?
)

