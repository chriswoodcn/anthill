package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.DefaultValue
import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class, JpaEntityListener::class)
@Entity
@Table(name = "sys_role")
data class SysRoleEntity(
    @Id
    @GeneratedValue
    val id: Int?,
    val roleNameJson: String?,
    val roleKey: String?,
    @DefaultValue("0")
    val roleSort: Int?,
    @DefaultValue("0")
    val status: String?,
    val comId: String?,
    val remarkJson: String?,
    @DefaultValue("2")
    val affiliateFlag: String?,

    @CreatedDate
    @Column(updatable = false, nullable = false)
    val createTime: Long = System.currentTimeMillis(),

    @LastModifiedDate
    @Column(nullable = false)
    val updateTime: Long = System.currentTimeMillis(),

    @Version
    @DefaultValue("0")
    val version: Int
)