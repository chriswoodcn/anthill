package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import cn.chriswood.anthill.infrastructure.web.base.R
import com.taotao.bmm.business.common.bo.SysConfigBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.service.SysConfigService
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysConfigVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/backend/config")
@Tag(name = "v1-backend-系统参数接口")
@Validated
class SysConfigController(
    private val sysConfigService: SysConfigService
) {
    @Operation(summary = "分页列表")
    @PostMapping("/page")
    fun page(
        @RequestBody @Validated(QueryGroup::class) bo: SysConfigBo, @RequestBody @Validated pageFrom: PageForm
    ): R<PageVo<SysConfigVo>> {
        return R.ok(sysConfigService.page(bo, pageFrom))
    }

    @Operation(summary = "创建")
    @PostMapping("/add")
    fun add(@RequestBody @Validated(AddGroup::class) bo: SysConfigBo): R<Int> {
        val id = sysConfigService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    fun update(@RequestBody @Validated(UpdateGroup::class) bo: SysConfigBo): R<Int> {
        val id = sysConfigService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "逻辑删除")
    @GetMapping("/deleteLogic/{ids}")
    fun deleteLogic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysConfigService.deleteLogic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "物理删除")
    @GetMapping("/deletePhysic/{ids}")
    fun deletePhysic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysConfigService.deletePhysic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "获取参数")
    @GetMapping("/get")
    fun get(
        @RequestParam @NotBlank(message = "{Validation.NotBlank}") key: String
    ): R<SysConfigVo?> {
        return R.ok(sysConfigService.get(key))
    }

    @Operation(summary = "刷新参数")
    @GetMapping("/refresh")
    fun refresh(@RequestParam key: String?): R<Unit> {
        return R.ok(sysConfigService.refreshCache(key))
    }
}