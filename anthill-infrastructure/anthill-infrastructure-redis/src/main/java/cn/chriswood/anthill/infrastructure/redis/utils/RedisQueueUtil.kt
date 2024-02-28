package cn.chriswood.anthill.infrastructure.redis.utils

import cn.hutool.extra.spring.SpringUtil
import org.redisson.api.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

object RedisQueueUtil {
    private val CLIENT: RedissonClient = SpringUtil.getBean(RedissonClient::class.java)
    fun getClient(): RedissonClient {
        return CLIENT
    }

    /**
     * 添加普通队列数据
     */
    fun <T> addQueueObject(queueName: String?, data: T & Any): Boolean {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        return queue.offer(data)
    }

    /**
     * 通用获取一个队列数据 没有数据返回 null(不支持延迟队列)
     */
    fun <T> getQueueObject(queueName: String): T? {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        return queue.poll()
    }

    /**
     * 通用删除队列数据(不支持延迟队列)
     */
    fun <T> removeQueueObject(queueName: String, data: T): Boolean {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        return queue.remove(data)
    }

    /**
     * 通用销毁队列 所有阻塞监听 报错(不支持延迟队列)
     */
    fun <T> destroyQueue(queueName: String): Boolean {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue<T>(queueName)
        return queue.delete()
    }

    /**
     * 添加延迟队列数据 默认毫秒
     */
    fun <T> addDelayedQueueObject(queueName: String, data: T, time: Long) {
        doAddDelayedQueueObject(queueName, data, time, TimeUnit.MILLISECONDS)
    }

    fun <T> doAddDelayedQueueObject(queueName: String, data: T, time: Long, timeUnit: TimeUnit?) {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        val delayedQueue: RDelayedQueue<T> = CLIENT.getDelayedQueue(queue)
        delayedQueue.offer(data, time, timeUnit)
    }

    /**
     * 获取一个延迟队列数据 没有数据返回 null
     */
    fun <T> getDelayedQueueObject(queueName: String): T? {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        val delayedQueue: RDelayedQueue<T> = CLIENT.getDelayedQueue(queue)
        return delayedQueue.poll()
    }

    /**
     * 删除延迟队列数据
     */
    fun <T> removeDelayedQueueObject(queueName: String, data: T): Boolean {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        val delayedQueue: RDelayedQueue<T> = CLIENT.getDelayedQueue(queue)
        return delayedQueue.remove(data)
    }

    /**
     * 销毁延迟队列 所有阻塞监听 报错
     */
    fun <T> destroyDelayedQueue(queueName: String) {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        val delayedQueue: RDelayedQueue<T> = CLIENT.getDelayedQueue(queue)
        delayedQueue.destroy()
    }

    /**
     * 添加优先队列数据
     */
    fun <T> addPriorityQueueObject(queueName: String, data: T & Any): Boolean {
        val priorityBlockingQueue: RPriorityBlockingQueue<T> =
            CLIENT.getPriorityBlockingQueue(queueName)
        return priorityBlockingQueue.offer(data)
    }

    /**
     * 优先队列获取一个队列数据 没有数据返回 null(不支持延迟队列)
     */
    fun <T> getPriorityQueueObject(queueName: String): T? {
        val queue: RPriorityBlockingQueue<T> =
            CLIENT.getPriorityBlockingQueue(queueName)
        return queue.poll()
    }

    /**
     * 优先队列删除队列数据(不支持延迟队列)
     */
    fun <T> removePriorityQueueObject(queueName: String, data: T): Boolean {
        val queue: RPriorityBlockingQueue<T> =
            CLIENT.getPriorityBlockingQueue(queueName)
        return queue.remove(data)
    }

    /**
     * 优先队列销毁队列 所有阻塞监听 报错(不支持延迟队列)
     */
    fun <T> destroyPriorityQueue(queueName: String): Boolean {
        val queue: RPriorityBlockingQueue<T> =
            CLIENT.getPriorityBlockingQueue(queueName)
        return queue.delete()
    }

    /**
     * 尝试设置 有界队列 容量 用于限制数量
     *
     * @param queueName 队列名
     * @param capacity  容量
     */
    fun <T> trySetBoundedQueueCapacity(queueName: String, capacity: Int): Boolean {
        val boundedBlockingQueue: RBoundedBlockingQueue<T> =
            CLIENT.getBoundedBlockingQueue(queueName)
        return boundedBlockingQueue.trySetCapacity(capacity)
    }

    /**
     * 尝试设置 有界队列 容量 用于限制数量
     *
     * @param queueName 队列名
     * @param capacity  容量
     * @param destroy   已存在是否销毁
     */
    fun <T> trySetBoundedQueueCapacity(queueName: String, capacity: Int, destroy: Boolean): Boolean {
        val boundedBlockingQueue: RBoundedBlockingQueue<T> =
            CLIENT.getBoundedBlockingQueue<T>(queueName)
        if (boundedBlockingQueue.isExists && destroy) {
            destroyQueue<Any>(queueName)
        }
        return boundedBlockingQueue.trySetCapacity(capacity)
    }

    /**
     * 添加有界队列数据
     *
     * @param queueName 队列名
     * @param data      数据
     * @return 添加成功 true 已达到界限 false
     */
    fun <T> addBoundedQueueObject(queueName: String, data: T & Any): Boolean {
        val boundedBlockingQueue: RBoundedBlockingQueue<T> =
            CLIENT.getBoundedBlockingQueue(queueName)
        return boundedBlockingQueue.offer(data)
    }

    /**
     * 有界队列获取一个队列数据 没有数据返回 null(不支持延迟队列)
     *
     * @param queueName 队列名
     */
    fun <T> getBoundedQueueObject(queueName: String): T? {
        val queue: RBoundedBlockingQueue<T> =
            CLIENT.getBoundedBlockingQueue(queueName)
        return queue.poll()
    }

    /**
     * 有界队列删除队列数据(不支持延迟队列)
     */
    fun <T> removeBoundedQueueObject(queueName: String, data: T): Boolean {
        val queue: RBoundedBlockingQueue<T> =
            CLIENT.getBoundedBlockingQueue(queueName)
        return queue.remove(data)
    }

    /**
     * 有界队列销毁队列 所有阻塞监听 报错(不支持延迟队列)
     */
    fun <T> destroyBoundedQueue(queueName: String): Boolean {
        val queue: RBoundedBlockingQueue<T> =
            CLIENT.getBoundedBlockingQueue(queueName)
        return queue.delete()
    }

    /**
     * 订阅阻塞队列(可订阅所有实现类 例如: 延迟 优先 有界 等)
     */
    fun <T> subscribeBlockingQueue(queueName: String?, isDelayed: Boolean, consumer: Consumer<T>?) {
        val queue: RBlockingQueue<T> = CLIENT.getBlockingQueue(queueName)
        if (isDelayed) {
            // 订阅延迟队列
            CLIENT.getDelayedQueue<T>(queue)
        }
        queue.subscribeOnElements(consumer)
    }
}
