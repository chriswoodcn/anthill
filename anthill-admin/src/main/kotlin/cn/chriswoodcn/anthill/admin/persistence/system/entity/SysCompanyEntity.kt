package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.DefaultValue
import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@EntityListeners(AuditingEntityListener::class, JpaEntityListener::class)
@Entity
@Table(name = "sys_company")
data class SysCompanyEntity(
    @Id
    val id: String?,
    val companyNameJson: String?,
    @DefaultValue("0")
    val status: String?,
    @DefaultValue("0")
    val templateId: Int?,
    @DefaultValue("0")
    val activeTime: Long?,

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