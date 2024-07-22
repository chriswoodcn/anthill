package com.taotao.bmm.persistence.system.entity

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
class RoleMenuId : Serializable {
    @Column(name = "role_id")
    var roleId: Int = 0

    @Column(name = "menu_id")
    var menuId: Int = 0

    override fun equals(other: Any?): Boolean {
        if (other is RoleMenuId) {
            return roleId == other.roleId && menuId == other.menuId
        }
        return false
    }

    override fun hashCode(): Int {
        return (menuId * roleId).hashCode()
    }
}

@Entity
@Table(name = "sys_role_menu")
data class SysRoleMenuRef(
    @EmbeddedId
    var id: RoleMenuId,
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    val role: SysRoleEntity? = null,
    @ManyToOne
    @MapsId("menuId")
    @JoinColumn(name = "menu_id")
    val menu: SysMenuEntity? = null
)