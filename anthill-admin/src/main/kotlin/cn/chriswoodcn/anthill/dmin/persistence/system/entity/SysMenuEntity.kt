package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.DefaultValue
import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class, JpaEntityListener::class)
@Entity
@Table(name = "sys_menu")
data class SysMenuEntity(
    @Id
    @GeneratedValue
    val id: Int?,
    val menuNameJson: String?,
    @DefaultValue("0")
    val parentId: Int?,
    @DefaultValue("0")
    val orderNum: Int?,
    val path: String?,
    val component: String?,
    @DefaultValue("M")
    val menuType: String?,
    @DefaultValue("0")
    val status: String?,
    val perms: String?,
    val icon: String?,
    val remarkJson: String?,
    val menuVersion: String?,
    @DefaultValue("0")
    val hiddenFlag: String?,
    @DefaultValue("0")
    val frameFlag: String?,
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
    val version: Int,
)