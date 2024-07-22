package com.taotao.bmm.persistence.system.entity

import jakarta.persistence.*
import java.io.Serializable


@Embeddable
class UserRoleId : Serializable {
    @Column(name = "user_id")
    var userId: Int = 0

    @Column(name = "role_id")
    var roleId: Int = 0

    override fun equals(other: Any?): Boolean {
        if (other is UserRoleId) {
            return roleId == other.roleId && userId == other.userId
        }
        return false
    }

    override fun hashCode(): Int {
        return (userId * roleId).hashCode()
    }
}

@Entity
@Table(name = "sys_user_role")
data class SysUserRoleRel(
    @EmbeddedId
    val id: UserRoleId,
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    val user: SysUserEntity? = null,
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    val role: SysRoleEntity? = null,
)