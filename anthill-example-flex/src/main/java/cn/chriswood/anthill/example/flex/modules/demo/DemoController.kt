package cn.chriswood.anthill.example.flex.modules.demo

import cn.chriswood.anthill.example.flex.store.TUserDs1Storage
import cn.chriswood.anthill.example.flex.store.TUserDs2Storage
import cn.chriswood.anthill.example.flex.support.genI18nExcelHead
import cn.chriswood.anthill.example.mybatisflex.dto.*
import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.chriswood.anthill.infrastructure.web.utils.HttpExcelUtil
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/demo")
class DemoController {

    private val log = LoggerFactory.getLogger(javaClass)


    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_primary")
    fun listPrimary(): R<List<TUserDs1Dto>> {
        return R.ok(TUserDs1Storage.list(TUserDs1Bo()))
    }

    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_slave1")
    fun listSlave1(): R<List<TUserDs2Dto>> {
        log.info("从ds2数据库读取数据")
        return R.ok(TUserDs2Storage.list(TUserDs2Bo()))
    }

    @Operation(summary = "添加")
    @PostMapping("/add")
    fun validate(@RequestBody @Validated(AddGroup::class) data: PostValidateData): R<Void> {
        log.debug("data = {}", data)
        return R.ok()
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    fun update(@RequestBody @Validated(UpdateGroup::class) data: PostValidateData): R<Void> {
        log.debug("data = {}", data)
        return R.ok()
    }

    @Operation(summary = "列表")
    @GetMapping
    fun list(data: GetValidateData, form: PageForm): R<Void> {
        log.debug("data = {}", data)
        return R.ok()
    }

    @Operation(summary = "查询")
    @GetMapping("/query")
    fun query(@Validated(QueryGroup::class) data: GetValidateData): R<Void> {
        log.debug("data = {}", data)
        return R.ok()
    }

    @Operation(summary = "导出")
    @GetMapping("/export")
    fun export(response: HttpServletResponse) {
        val total = TUserDs1Storage.count<TUserDs1Bo, TUserDs1Dto>(TUserDs1Bo(), emptyList())
        val ds = HttpExcelUtil.DataSource<TUserDs1Data>(
            1,
            1000,
            total,
            genI18nExcelHead<TUserDs1Data>(),
            null
        ) { pageNum, pageSize ->
            val page = TUserDs1Storage.page<TUserDs1Bo, TUserDs1Dto>(pageNum.toInt(), pageSize.toInt(), TUserDs1Bo())
            if (page.records.size > 0) {
                page.records.map(TUserDs1Storage::convert2Data)
            } else {
                null
            }
        }
        HttpExcelUtil.export(
            response, "TUserDs1Data", ds, TUserDs1Data::class
        ).let {
            if (!it) {
                println("HttpExcelUtil export fail")
            }
        }
    }
}
