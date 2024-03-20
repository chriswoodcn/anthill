
import cn.hutool.core.date.SystemClock
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.test.Test

class TestCoroutine {
    @Test
    fun test() {
        runBlocking {
            repeat(50_000) { // 启动大量的协程
                launch {
                    delay(5000L)
                    print(".")
                }
            }
        }
        runBlocking {
//            launch { // launch a new coroutine and continue
//                delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
//                println("World!") // print after delay
//            }
//            val job = launch {
//                delay(1000L)
//                println("World!")
//            }
            println(SystemClock.nowDate())
            doWorld1()
            println("Hello") // c
            println(SystemClock.nowDate())
//            job.join() // wait until child coroutine completes
//            println(SystemClock.nowDate())
            println("finish") // c
        }
    }

    suspend fun doWorld1() {
        delay(2000L)
        println("World 2")
        delay(1000L)
        println("World 1")
    }

    // Concurrently executes both sections
    suspend fun doWorld2() = coroutineScope {
        launch {
            delay(2000L)
            println("World 2")
        }
        launch {
            delay(1000L)
            println("World 1")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testChannel() {
        runBlocking {
            val channel = Channel<Int>()
            launch {
                // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
                for (x in 1..5) channel.send(x * x)
                channel.close() // 我们结束发送
            }
//            // 这里我们打印了 5 次被接收的整数：
//            repeat(5) { println(channel.receive()) }
            // 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
            for (y in channel) println(y)
            println("Done!")
        }

        //简化版本
        runBlocking {
//sampleStart
            val squares = produceSquares()
            squares.consumeEach { println(it) }
            println("Done!")
//sampleEnd
        }

        runBlocking {
//sampleStart
            val numbers = produce {
                var x = 10
                while (true) send(x++) // 在流中开始从 1 生产无穷多个整数
            }
            val squares = produce {
                for (x in numbers) send(x * x)
            }
            repeat(5) {
                println(squares.receive()) // 输出前五个
            }
            println("Done!") // 至此已完成
            coroutineContext.cancelChildren() // 取消子协程
//sampleEnd
        }

        runBlocking {
//sampleStart
            var cur = numbersFrom(2)
            repeat(10) {
                val prime = cur.receive()
                println(prime)
                cur = filter(cur, prime)
            }
            coroutineContext.cancelChildren() // 取消所有的子协程来让主协程结束
//sampleEnd
        }
        runBlocking {
//sampleStart
            val producer = produceNumbers()
            repeat(5) { launchProcessor(it, producer) }
            delay(950)
            producer.cancel() // 取消协程生产者从而将它们全部杀死
//sampleEnd
        }


        runBlocking {
//sampleStart
            val channel = Channel<String>()
            launch { sendString(channel, "foo", 200L) }
            launch { sendString(channel, "BAR!", 500L) }
            repeat(6) { // 接收前六个
                println(channel.receive())
            }
            coroutineContext.cancelChildren() // 取消所有子协程来让主协程结束
//sampleEnd
        }

        runBlocking<Int> {
//sampleStart
            val channel = Channel<Int>(4) // 启动带缓冲的通道
            val sender = launch { // 启动发送者协程
                repeat(10) {
                    println("Sending $it") // 在每一个元素发送前打印它们
                    channel.send(it) // 将在缓冲区被占满时挂起
                }
            }
            // 没有接收到东西……只是等待……
            delay(1000)
            sender.cancel() // 取消发送者协程
//sampleEnd
            0
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
        for (x in 1..5) send(x * x)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
        var x = start
        while (true) send(x++) // 从 start 开始过滤整数流
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
        for (x in numbers) if (x % prime != 0) send(x)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1 // 从 1 开始
        while (true) {
            send(x++) // 产生下一个数字
            delay(100) // 等待 0.1 秒
        }
    }

    fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
        for (msg in channel) {
            println("Processor #$id received $msg")
        }
    }

    suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }

    //sampleStart
    data class Ball(var hits: Int)

    @Test
    fun testBall() = runBlocking {
        val table = Channel<Ball>() // 一个共享的 table（桌子）
        launch { player("ping", table) }
        launch { player("pong", table) }
        table.send(Ball(0)) // 乒乓球
        delay(1000) // 延迟 1 秒钟
        coroutineContext.cancelChildren() // 游戏结束，取消它们
    }

    suspend fun player(name: String, table: Channel<Ball>) {
        for (ball in table) { // 在循环中接收球
            ball.hits++
            println("$name $ball")
            delay(100) // 等待一段时间
            table.send(ball) // 将球发送回去
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    @Test
    fun testTicker() = runBlocking<Unit> {
        val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
        var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
        println("Initial element is available immediately: $nextElement") // no initial delay

        nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } // all subsequent elements have 100ms delay
        println("Next element is not ready in 50 ms: $nextElement")

        nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
        println("Next element is ready in 100 ms: $nextElement")

        // 模拟大量消费延迟
        println("Consumer pauses for 150ms")
        delay(150)
        // 下一个元素立即可用
        nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
        println("Next element is available immediately after large consumer delay: $nextElement")
        // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
        nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
        println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

        tickerChannel.cancel() // 表明不再需要更多的元素
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    fun testCoroutineContext() {
        val threadLocal = ThreadLocal<String>()
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        threadLocal.set("testCoroutineContext")
        println("threadLocal = ${threadLocal.get()}")
        runBlocking {
            println("main runBlocking main")
            println("threadLocal = ${threadLocal.get()}")
//sampleStart
            launch { // 运行在父协程的上下文中，即 runBlocking 主协程
                println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
                println("threadLocal = ${threadLocal.get()}")
            }
            launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
                println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
                println("threadLocal = ${threadLocal.get()}")
            }
            launch(Dispatchers.Default) { // 将会获取默认调度器
                println("Default               : I'm working in thread ${Thread.currentThread().name}")
                println("threadLocal = ${threadLocal.get()}")
            }
            launch(Dispatchers.IO) { // 将会获取默认调度器
                println("IO               : I'm working in thread ${Thread.currentThread().name}")
                println("threadLocal = ${threadLocal.get()}")
            }
            launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
                println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
                println("threadLocal = ${threadLocal.get()}")
            }
//sampleEnd
        }
    }
}
