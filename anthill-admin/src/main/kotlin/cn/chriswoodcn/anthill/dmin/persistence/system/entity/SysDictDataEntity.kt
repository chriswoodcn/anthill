package com.taotao.bmm.persistence.system.entity

import com.taotao.bmm.persistence.system.DefaultValue
import com.taotao.bmm.persistence.system.JpaEntityListener
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@EntityListeners(AuditingEntityListener::class, JpaEntityListener::class)
@Entity
@Table(name = "sys_dict_data")
data class SysDictDataEntity(
    @Id
    @GeneratedValue
    val id: Int?,
    @DefaultValue("0")
    val dictSort: Int?,
    val dictLabelJson: String?,
    val dictValue: String?,
    val dictType: String?,
    @DefaultValue("0")
    val status: String?,
    val remarkJson: String?,

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