package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.core.validate.group.*
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import com.taotao.bmm.business.common.bo.SysRoleBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.service.SysRoleService
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysRoleVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/backend/role")
@Tag(name = "v1-backend-角色接口")
@Validated
class SysRoleController(
    private val sysRoleService: SysRoleService
) {
    @Operation(summary = "获取角色选择数据")
    @GetMapping("/select")
    @SaIgnore
    fun roleSelect(
        @NotBlank(message = "{Validation.NotBlank}")
        @RequestParam flag: String,
        @RequestParam comId: String?
    ): R<List<SysRoleVo>> {
        return R.ok(sysRoleService.getRoleSelectByUserType(flag, comId))
    }

    @Operation(summary = "分页列表")
    @PostMapping("/page")
    fun page(
        @RequestBody @Validated(QueryGroup::class) bo: SysRoleBo,
        @RequestBody @Validated pageFrom: PageForm
    ): R<PageVo<SysRoleVo>> {
        return R.ok(sysRoleService.page(bo, pageFrom))
    }

    @Operation(summary = "创建")
    @PostMapping("/add")
    fun add(@RequestBody @Validated(AddGroup::class) bo: SysRoleBo): R<Int> {
        val id = sysRoleService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    fun update(@RequestBody @Validated(UpdateGroup::class) bo: SysRoleBo): R<Int> {
        val id = sysRoleService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "逻辑删除")
    @GetMapping("/deleteLogic/{ids}")
    fun deleteLogic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysRoleService.deleteLogic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "物理删除")
    @GetMapping("/deletePhysic/{ids}")
    fun deletePhysic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysRoleService.deletePhysic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }
}