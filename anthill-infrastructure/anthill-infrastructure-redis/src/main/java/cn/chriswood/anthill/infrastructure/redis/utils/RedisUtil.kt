package cn.chriswood.anthill.infrastructure.redis.utils

import cn.hutool.extra.spring.SpringUtil
import org.redisson.api.*
import java.time.Duration
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream

object RedisUtil {

    val CLIENT: RedissonClient = SpringUtil.getBean(RedissonClient::class.java)

    fun getClient(): RedissonClient {
        return CLIENT
    }

    /**
     * 限流
     *
     * @param key          限流key
     * @param rateType     限流类型
     * @param rate         速率
     * @param rateInterval 速率间隔
     * @return -1 表示失败
     */
    fun rateLimit(key: String, rateType: RateType, rate: Int, rateInterval: Int): Long {
        val rateLimiter: RRateLimiter = CLIENT.getRateLimiter(key)
        rateLimiter.trySetRate(rateType, rate.toLong(), rateInterval.toLong(), RateIntervalUnit.SECONDS)
        return if (rateLimiter.tryAcquire()) {
            rateLimiter.availablePermits()
        } else {
            -1L
        }
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    fun <T> publish(channelKey: String, msg: T, consumer: Consumer<T>) {
        val topic: RTopic = CLIENT.getTopic(channelKey)
        topic.publish(msg)
        consumer.accept(msg)
    }

    fun <T> publish(channelKey: String, msg: T) {
        val topic: RTopic = CLIENT.getTopic(channelKey)
        topic.publish(msg)
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    fun <T> subscribe(channelKey: String, clazz: Class<T>, consumer: Consumer<T>) {
        val topic: RTopic = CLIENT.getTopic(channelKey)
        topic.addListener(clazz) { _: CharSequence, msg: T ->
            consumer.accept(
                msg
            )
        }
    }


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    fun <T> setCacheObject(key: String, value: T) {
        setCacheObjectSaveTtl(key, value, false)
    }

    /**
     * 缓存基本的对象，保留当前对象 TTL 有效期
     *
     * @param key       缓存的键值
     * @param value     缓存的值
     * @param isSaveTtl 是否保留TTL有效期(例如: set之前ttl剩余90 set之后还是为90)
     * @since Redis 6.X 以上使用 setAndKeepTTL 兼容 5.X 方案
     */
    fun <T> setCacheObjectSaveTtl(key: String, value: T, isSaveTtl: Boolean) {
        val bucket: RBucket<T> = CLIENT.getBucket<T>(key)
        if (isSaveTtl) {
            try {
                bucket.setAndKeepTTL(value)
            } catch (e: Exception) {
                val timeToLive = bucket.remainTimeToLive()
                setCacheObjectTtl(key, value, Duration.ofMillis(timeToLive))
            }
        } else {
            bucket.set(value)
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    fun <T> setCacheObjectTtl(key: String, value: T, duration: Duration) {
        val batch: RBatch = CLIENT.createBatch()
        val bucket = batch.getBucket<T>(key)
        bucket.setAsync(value)
        bucket.expireAsync(duration)
        batch.execute()
    }

    /**
     * 如果不存在则设置 并返回 true 如果存在则返回 false
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return set成功或失败
     */
    fun <T> setObjectIfAbsent(key: String, value: T, duration: Duration?): Boolean {
        val bucket: RBucket<T> = CLIENT.getBucket(key)
        return bucket.setIfAbsent(value, duration)
    }

    /**
     * 如果存在则设置 并返回 true 如果存在则返回 false
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return set成功或失败
     */
    fun <T> setObjectIfExists(key: String, value: T, duration: Duration?): Boolean {
        val bucket: RBucket<T> = CLIENT.getBucket(key)
        return bucket.setIfExists(value, duration)
    }

    /**
     * 注册对象监听器
     *
     *
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    fun <T> addObjectListener(key: String, listener: ObjectListener) {
        val result: RBucket<T> = CLIENT.getBucket(key)
        result.addListener(listener)
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    fun expire(key: String, timeout: Long): Boolean {
        return expireDuration(key, Duration.ofSeconds(timeout))
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 超时时间
     * @return true=设置成功；false=设置失败
     */
    fun expireDuration(key: String, duration: Duration?): Boolean {
        val rBucket: RBucket<*> = CLIENT.getBucket<Any>(key)
        return rBucket.expire(duration)
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    fun <T> getCacheObject(key: String): T {
        val rBucket: RBucket<T> = CLIENT.getBucket(key)
        return rBucket.get()
    }

    /**
     * 获得key剩余存活时间
     *
     * @param key 缓存键值
     * @return 剩余存活时间
     */
    fun <T> getTimeToLive(key: String): Long {
        val rBucket: RBucket<T> = CLIENT.getBucket(key)
        return rBucket.remainTimeToLive()
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    fun deleteObject(key: String): Boolean {
        return CLIENT.getBucket<Any>(key).delete()
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    fun deleteObject(collection: Collection<Any>) {
        val batch: RBatch = CLIENT.createBatch()
        collection.forEach {
            batch.getBucket<Any>(it.toString()).deleteAsync()
        }
        batch.execute()
    }

    /**
     * 检查缓存对象是否存在
     *
     * @param key 缓存的键值
     */
    fun isExistsObject(key: String?): Boolean {
        return CLIENT.getBucket<Any>(key).isExists
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    fun <T> setCacheList(key: String, dataList: List<T>?): Boolean {
        val rList: RList<T> = CLIENT.getList(key)
        return rList.addAll(dataList!!)
    }

    /**
     * 追加缓存List数据
     *
     * @param key  缓存的键值
     * @param data 待缓存的数据
     * @return 缓存的对象
     */
    fun <T> addCacheList(key: String, data: T): Boolean {
        val rList: RList<T> = CLIENT.getList(key)
        return rList.add(data)
    }

    /**
     * 注册List监听器
     *
     *
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    fun <T> addListListener(key: String, listener: ObjectListener?) {
        val rList: RList<T> = CLIENT.getList(key)
        rList.addListener(listener)
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    fun <T> getCacheList(key: String): List<T> {
        val rList: RList<T> = CLIENT.getList(key)
        return rList.readAll()
    }

    /**
     * 获得缓存的list对象(范围)
     *
     * @param key  缓存的键值
     * @param form 起始下标
     * @param to   截止下标
     * @return 缓存键值对应的数据
     */
    fun <T> getCacheListRange(key: String, form: Int, to: Int): List<T> {
        val rList: RList<T> = CLIENT.getList(key)
        return rList.range(form, to)
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    fun <T> setCacheSet(key: String, dataSet: Set<T>?): Boolean {
        if (dataSet == null) return false
        val rSet: RSet<T> = CLIENT.getSet(key)
        return rSet.addAll(dataSet)
    }

    /**
     * 追加缓存Set数据
     *
     * @param key  缓存的键值
     * @param data 待缓存的数据
     * @return 缓存的对象
     */
    fun <T> addCacheSet(key: String, data: T): Boolean {
        val rSet: RSet<T> = CLIENT.getSet(key)
        return rSet.add(data)
    }

    /**
     * 注册Set监听器
     *
     *
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    fun <T> addSetListener(key: String, listener: ObjectListener?) {
        val rSet: RSet<T> = CLIENT.getSet(key)
        rSet.addListener(listener)
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存的key
     * @return set对象
     */
    fun <T> getCacheSet(key: String): Set<T> {
        val rSet: RSet<T> = CLIENT.getSet(key)
        return rSet.readAll()
    }

    /**
     * 缓存Map
     *
     * @param key     缓存的键值
     * @param dataMap 缓存的数据
     */
    fun <T> setCacheMap(key: String, dataMap: Map<String, T>?) {
        if (dataMap != null) {
            val rMap: RMap<String, T> = CLIENT.getMap(key)
            rMap.putAll(dataMap)
        }
    }

    /**
     * 注册Map监听器
     *
     *
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    fun <T> addMapListener(key: String, listener: ObjectListener?) {
        val rMap: RMap<String, T> = CLIENT.getMap(key)
        rMap.addListener(listener)
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    fun <T> getCacheMap(key: String): Map<String, T> {
        val rMap: RMap<String, T> = CLIENT.getMap(key)
        return rMap.getAll(rMap.keys)
    }

    /**
     * 获得缓存Map的key列表
     *
     * @param key 缓存的键值
     * @return key列表
     */
    fun <T> getCacheMapKeySet(key: String): Set<String> {
        val rMap: RMap<String, T> = CLIENT.getMap(key)
        return rMap.keys
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    fun <T> setCacheMapValue(key: String, hKey: String, value: T) {
        val rMap: RMap<String, T> = CLIENT.getMap(key)
        rMap[hKey] = value
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    fun <T> getCacheMapValue(key: String, hKey: String): T? {
        val rMap: RMap<String, T> = CLIENT.getMap(key)
        return rMap[hKey]
    }

    /**
     * 删除Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    fun <T> delCacheMapValue(key: String, hKey: String): T? {
        val rMap: RMap<String, T> = CLIENT.getMap(key)
        return rMap.remove(hKey)
    }

    /**
     * 删除Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键
     */
    fun <T> delMultiCacheMapValue(key: String, hKeys: Set<String>) {
        val batch: RBatch = CLIENT.createBatch()
        val rMap = batch.getMap<String, T>(key)
        for (hKey in hKeys) {
            rMap.removeAsync(hKey)
        }
        batch.execute()
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    fun <K, V> getMultiCacheMapValue(key: String, hKeys: Set<K>): Map<K, V> {
        val rMap: RMap<K, V> = CLIENT.getMap(key)
        return rMap.getAll(hKeys)
    }

    /**
     * 设置原子值
     *
     * @param key   Redis键
     * @param value 值
     */
    fun setAtomicValue(key: String, value: Long) {
        val atomic: RAtomicLong = CLIENT.getAtomicLong(key)
        atomic.set(value)
    }

    /**
     * 获取原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    fun getAtomicValue(key: String): Long {
        val atomic: RAtomicLong = CLIENT.getAtomicLong(key)
        return atomic.get()
    }

    /**
     * 递增原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    fun incrAtomicValue(key: String): Long {
        val atomic: RAtomicLong = CLIENT.getAtomicLong(key)
        return atomic.incrementAndGet()
    }

    /**
     * 递减原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    fun decrAtomicValue(key: String): Long {
        val atomic: RAtomicLong = CLIENT.getAtomicLong(key)
        return atomic.decrementAndGet()
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    fun keys(pattern: String): Collection<String> {
        val stream: Stream<String> =
            CLIENT.keys.getKeysStreamByPattern(pattern)
        return stream.collect(Collectors.toList())
    }

    /**
     * 删除缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     */
    fun deleteKeys(pattern: String) {
        CLIENT.keys.deleteByPattern(pattern)
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 键
     */
    fun hasKey(key: String): Boolean {
        val rKeys: RKeys = CLIENT.keys
        return rKeys.countExists(key) > 0
    }
}
