package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import cn.chriswood.anthill.infrastructure.web.base.R
import com.taotao.bmm.business.common.bo.SysDictTypeBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.service.SysDictTypeService
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysDictTypeVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/backend/dict/type")
@Tag(name = "v1-backend-字典类型接口")
class SysDictTypeController(
    private val sysDictTypeService: SysDictTypeService
) {
    @Operation(summary = "分页列表")
    @PostMapping("/page")
    fun page(
        @RequestBody @Validated(QueryGroup::class) bo: SysDictTypeBo,
        @RequestBody @Validated pageFrom: PageForm
    ): R<PageVo<SysDictTypeVo>> {
        return R.ok(sysDictTypeService.page(bo, pageFrom))
    }

    @Operation(summary = "创建")
    @PostMapping("/add")
    fun add(@RequestBody @Validated(AddGroup::class) bo: SysDictTypeBo): R<Int> {
        val id = sysDictTypeService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    fun update(@RequestBody @Validated(UpdateGroup::class) bo: SysDictTypeBo): R<Int> {
        val id = sysDictTypeService.save(bo)
        return if (id > 0) R.ok(id) else R.fail()
    }

    @Operation(summary = "逻辑删除")
    @GetMapping("/deleteLogic/{ids}")
    fun deleteLogic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysDictTypeService.deleteLogic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "物理删除")
    @GetMapping("/deletePhysic/{ids}")
    fun deletePhysic(@PathVariable ids: Array<Int>): R<Int> {
        val num = sysDictTypeService.deletePhysic(ids)
        return if (num > 0) R.ok(num) else R.fail()
    }

    @Operation(summary = "刷新")
    @GetMapping("/refresh")
    fun refresh(@RequestParam key: String?): R<Unit> {
        return R.ok(sysDictTypeService.refreshCache(key))
    }
}