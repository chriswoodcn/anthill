package cn.chriswood.anthill.infrastructure.push.support

import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object PushPool {
    private val log = LoggerFactory.getLogger(javaClass)

    private val pushTemplates: ConcurrentMap<String, MutableList<PushTemplate>> = ConcurrentHashMap()

    private var hasFetchBeans = false

    private var availablePushTemplate = ThreadLocal<PushTemplate>()

    init {
        init()
    }

    private fun init() {
        val beans = SpringUtil.getBeansOfType(PushTemplate::class.java)
        beans.forEach {
            addPushTemplate(it.value.getKey(), it.value)
        }
        hasFetchBeans = true
        pushTemplates.forEach { (k, v) ->
            log.debug(">>>>>>>>>> PushPool fetch pushTemplates key = $k , size = ${v.size} >>>>>>>>>>")
        }
    }

    fun addPushTemplate(key: String, account: PushTemplate) {
        var list = pushTemplates[key]
        if (null == list) {
            pushTemplates[key] = mutableListOf()
            list = pushTemplates[key]
            list!!.add(account)
        } else {
            val existSuppliers = list.map { it.getSupplier() }
            if (existSuppliers.contains(account.getSupplier())) {
                list.removeIf { it.getSupplier() == account.getSupplier() }
            }
            list.add(account)
        }
    }

    fun removePushTemplate(key: String, supplier: String) {
        val list = pushTemplates[key]
        if (!list.isNullOrEmpty())
            list.removeIf { it.getSupplier() == supplier }
    }

    fun addPushTemplates(key: String, accounts: Collection<PushTemplate>) {
        var list = pushTemplates[key]
        if (null == list) {
            pushTemplates[key] = mutableListOf()
            list = pushTemplates[key]
            list!!.addAll(accounts)
        } else {
            val existSuppliers = list.map { it.getSupplier() }
            accounts.forEach { t ->
                if (existSuppliers.contains(t.getSupplier()))
                    list.removeIf { it.getSupplier() == t.getSupplier() }
            }
            list.addAll(accounts)
        }

    }

    fun removePushTemplates(key: String, suppliers: Collection<String>) {
        val list = pushTemplates[key]
        if (!list.isNullOrEmpty()) {
            suppliers.forEach { s ->
                list.removeIf { it.getSupplier() == s }
            }
        }

    }

    fun getAvailablePushTemplate(key: String, supplier: String): PushPool {
        if (!hasFetchBeans && pushTemplates.isEmpty()) {
            init()
        }
        if (pushTemplates.isEmpty()) {
            throw PushException("Push Pool is empty")
        }
        val account = pushTemplates[key]
        if (!account.isNullOrEmpty()) {
            val filter = account.filter {
                it.getSupplier() == supplier
            }
            if (filter.isEmpty()) {
                throw PushException("No Available Push Template")
            }
            this.availablePushTemplate.set(filter[0])
            return this
        } else {
            throw PushException("Pool Template is empty")
        }
    }

    fun sendPush(message: Map<String, Any>): Boolean {
        return try {
            val pushTemplate = availablePushTemplate.get()
            pushTemplate.sendPush(message)
            true
        } catch (_: Exception) {
            false
        }
    }

}
