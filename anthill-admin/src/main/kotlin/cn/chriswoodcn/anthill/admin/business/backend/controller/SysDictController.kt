package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.web.base.R
import com.taotao.bmm.business.common.service.SysDictTypeService
import com.taotao.bmm.business.common.vo.SysDictDataVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/backend/dict")
@Tag(name = "v1-backend-字典接口")
@Validated
class SysDictController(
    private val sysDictTypeService: SysDictTypeService
) {
    @Operation(summary = "根据字典类型获取字典数据")
    @GetMapping
    fun getDict(
        @RequestParam
        @NotBlank(message = "{Validation.NotBlank}")
        type: String
    ): R<List<SysDictDataVo>> {
        return R.ok(sysDictTypeService.get(type))
    }
}