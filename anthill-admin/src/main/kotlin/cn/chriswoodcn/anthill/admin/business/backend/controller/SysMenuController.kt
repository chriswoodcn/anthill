package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import com.taotao.bmm.business.common.bo.SysMenuBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.service.SysMenuService
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysMenuVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/backend/menu")
@Tag(name = "v1-backend-菜单接口")
@Validated
class SysMenuController(
    private val sysMenuService: SysMenuService,
) {
    @Operation(summary = "获取菜单选择数据")
    @GetMapping("/select")
    @SaIgnore
    fun menuSelect(
        @NotBlank(message = "{Validation.NotBlank}")
        @RequestParam flag: String
    ): R<List<SysMenuVo>> {
        return R.ok(sysMenuService.getMenuSelectByUserType(flag))
    }

    @Operation(summary = "列表")
    @PostMapping("/list")
    fun list(
        @RequestBody @Validated(QueryGroup::class) bo: SysMenuBo,
    ): R<List<SysMenuVo>> {
        return R.ok(sysMenuService.list(bo))
    }

    @Operation(summary = "分页列表")
    @PostMapping("/page")
    fun page(
        @RequestBody @Validated(QueryGroup::class) bo: SysMenuBo,
        @RequestBody @Validated pageFrom: PageForm
    ): R<PageVo<SysMenuVo>> {
        return R.ok(sysMenuService.page(bo, pageFrom))
    }

    @Operation(summary = "创建")
    @PostMapping("/add")
    fun add(@RequestBody @Validated(AddGroup::class) bo: SysMenuBo): R<Int> {
        val id = sysMenuService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    fun update(@RequestBody @Validated(UpdateGroup::class) bo: SysMenuBo): R<Int> {
        val id = sysMenuService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "逻辑删除")
    @GetMapping("/deleteLogic/{ids}")
    fun deleteLogic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysMenuService.deleteLogic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "物理删除")
    @GetMapping("/deletePhysic/{ids}")
    fun deletePhysic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysMenuService.deletePhysic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }
}