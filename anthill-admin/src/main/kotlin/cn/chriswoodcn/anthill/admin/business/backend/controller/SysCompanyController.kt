package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import com.taotao.bmm.business.common.bo.SysCompanyBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.service.SysCompanyService
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysCompanyVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

@RestController
@RequestMapping("/backend/company")
@Tag(name = "v1-backend-客户接口")
class SysCompanyController(
    private val sysCompanyService: SysCompanyService,
) {
    @Operation(summary = "获取客户选择数据")
    @GetMapping("/select")
    @SaIgnore
    fun select(): R<List<SysCompanyVo>> {
        return R.ok(sysCompanyService.getSelect())
    }

    @Operation(summary = "列表")
    @PostMapping("/list")
    fun list(
        @RequestBody @Validated(QueryGroup::class) bo: SysCompanyBo,
    ): R<List<SysCompanyVo>> {
        return R.ok(sysCompanyService.list(bo))
    }

    @Operation(summary = "分页列表")
    @PostMapping("/page")
    fun page(
        @RequestBody @Validated(QueryGroup::class) bo: SysCompanyBo, @RequestBody @Validated pageFrom: PageForm
    ): R<PageVo<SysCompanyVo>> {
        return R.ok(sysCompanyService.page(bo, pageFrom))
    }

    @Operation(summary = "创建")
    @PostMapping("/add")
    fun add(@RequestBody @Validated(AddGroup::class) bo: SysCompanyBo): R<String> {
        val id = sysCompanyService.add(bo)
        return if (!id.isNullOrBlank()) R.ok(id) else R.fail()
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    fun update(@RequestBody @Validated(UpdateGroup::class) bo: SysCompanyBo): R<String> {
        val id = sysCompanyService.update(bo)
        return if (!id.isNullOrBlank()) R.ok(id) else R.fail()
    }

    @Operation(summary = "逻辑删除")
    @GetMapping("/deleteLogic/{ids}")
    fun deleteLogic(@PathVariable ids: Array<String>): R<Int> {
        val num = sysCompanyService.deleteLogic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    fun export(
        @RequestBody @Validated(QueryGroup::class) bo: SysCompanyBo,
    ): ResponseEntity<StreamingResponseBody> {
        val data = StreamingResponseBody {
            sysCompanyService.export(bo, it)
        }
        return ResponseEntity(data, HttpStatus.OK)
    }
}