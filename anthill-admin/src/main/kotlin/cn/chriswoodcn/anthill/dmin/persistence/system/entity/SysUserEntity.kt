package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.DefaultValue
import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class, JpaEntityListener::class)
@Entity
@Table(name = "sys_user")
data class SysUserEntity(
    @Id
    @GeneratedValue
    val id: Int?,
    val username: String?,
    val nickname: String?,
    @DefaultValue("1")
    val userType: String?,
    val email: String?,
    val mobile: String?,
    @DefaultValue("2")
    val sex: String?,
    val avatar: String?,
    val password: String?,
    @DefaultValue("0")
    val status: String?,
    val loginIp: String?,
    val loginTime: Long?,
    val comId: String?,
    @DefaultValue("0")
    val adminFlag: String = "0",

    @CreatedDate
    @Column(updatable = false, nullable = false)
    val createTime: Long = System.currentTimeMillis(),

    @LastModifiedDate
    @Column(nullable = false)
    val updateTime: Long = System.currentTimeMillis(),

    @Version
    @DefaultValue("0")
    val version: Int,
)