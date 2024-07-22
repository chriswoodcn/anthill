package com.taotao.bmm.business.common.runner

import com.taotao.bmm.business.common.service.SysConfigService
import com.taotao.bmm.business.common.service.SysDictTypeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * 服务初始化runner
 * 加载配置
 * 加载字典
 */
@Component
class CacheRunner(
    private val sysConfigService: SysConfigService,
    private val sysDictTypeService: SysDictTypeService
) : ApplicationRunner {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    override fun run(args: ApplicationArguments?) {
        sysConfigService.refreshCache()
        log.info(">>>>>>>>>> load cache sys config >>>>>>>>>>")
        sysDictTypeService.refreshCache()
        log.info(">>>>>>>>>> load cache sys dict >>>>>>>>>>")
    }
}