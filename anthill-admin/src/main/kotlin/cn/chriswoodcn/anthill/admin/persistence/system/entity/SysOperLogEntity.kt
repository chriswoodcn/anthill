package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.DefaultValue
import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@EntityListeners(JpaEntityListener::class)
@Entity
@Table(name = "sys_oper_log")
data class SysOperLogEntity(
    @Id
    @GeneratedValue
    val id: Long?,
    val title: String?,
    @DefaultValue("0")
    val businessType: String?,
    val method: String?,
    val requestMethod: String?,
    @DefaultValue("0")
    val operatorUserType: String?,
    val operUser: String?,
    val operUrl: String?,
    val operIp: String?,
    val operLocation: String?,
    val operParam: String?,
    val jsonResult: String?,
    val status: String?,
    val errorMsg: String?,
    val operTime: Long?
)
