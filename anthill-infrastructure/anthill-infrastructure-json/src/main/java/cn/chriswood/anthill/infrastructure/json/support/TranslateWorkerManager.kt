package cn.chriswood.anthill.infrastructure.json.support

import cn.chriswood.anthill.infrastructure.json.annotation.Translate
import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object TranslateWorkerManager {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 存储WORKER的池
     */
    private val WORKER_POOL: ConcurrentMap<String, TranslateWorker> = ConcurrentHashMap()

    /**
     * 存储WORKER的key值
     */
    private val WORKER_POOL_KEYS: List<String> = mutableListOf()


    /**
     * 初始化
     */
    fun initPool() {
        log.debug(">>>>>>>>>> init TranslateWorkerManager >>>>>>>>>>")
        val workerSource = SpringUtil.getBeansOfType(TranslateWorker::class.java)
        val workerList = workerSource.map { it.value }.toList()
        workerList.forEach {
            val annotation = it.javaClass.getAnnotation(Translate::class.java) ?: null
            if (null == annotation) log.warn(
                "{} not mark Translate annotation, application can not identify it to translate.", it.javaClass.name
            )
            if (annotation!!.key.isNotEmpty()) {
                WORKER_POOL.plus(annotation.key to it)
                WORKER_POOL_KEYS.plus(annotation.key)
            } else log.warn(
                "{} Translate annotation key is empty", it.javaClass.name
            )
        }
    }

    /**
     * 获取可用的WORKER
     */
    fun getAvailableTranslateWorker(key: String): TranslateWorker? {
        return WORKER_POOL[key]
    }

}
