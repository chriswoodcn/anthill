package cn.chriswood.anthill.infrastructure.json

import cn.chriswood.anthill.infrastructure.json.support.TranslateWorkerManager
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.jackson",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class JsonConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init TranslateWorkerManager >>>>>>>>>>")
        TranslateWorkerManager.initPool()
    }
}
