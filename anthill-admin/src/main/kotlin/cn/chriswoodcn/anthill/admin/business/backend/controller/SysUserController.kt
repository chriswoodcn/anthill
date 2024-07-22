package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import cn.chriswood.anthill.infrastructure.web.base.R
import com.taotao.bmm.business.common.bo.SysUserBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.service.SysUserService
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysUserVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/backend/user")
@Tag(name = "v1-backend-用户接口")
class SysUserController(
    private val sysUserService: SysUserService
) {
    @Operation(summary = "分页列表")
    @PostMapping("/page")
    fun page(
        @RequestBody @Validated(QueryGroup::class) bo: SysUserBo,
        @RequestBody @Validated pageFrom: PageForm
    ): R<PageVo<SysUserVo>> {
        return R.ok(sysUserService.page(bo, pageFrom))
    }

    @Operation(summary = "创建")
    @PostMapping("/add")
    fun add(@RequestBody @Validated(AddGroup::class) bo: SysUserBo): R<Int> {
        val id = sysUserService.add(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    fun update(@RequestBody @Validated(AddGroup::class) bo: SysUserBo): R<Int> {
        val id = sysUserService.update(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "逻辑删除")
    @GetMapping("/deleteLogic/{ids}")
    fun deleteLogic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysUserService.deleteLogic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }
}