import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test


class TestFlow {

    @Test
    fun flow_basic() {
        /**
         * Flow冷流：主动需要即是主动收集才会提供发射数据
         * Channel热流：不管你需不需要一上来数据全都发射给你
         */
        /**
         * Flow构建器
         */
        //不需要用挂起函数修饰符
        fun flowBuilder() = flow {
            for (i in 1..3) {
                delay(1000)//可以使用挂起函数
                emit(i)//发射元素
            }
        }

        //flowOf 不需要挂起函数修饰符 flowOf自动实现发射元素
        fun flowOfBuilder() = flowOf(1, 2, 3, 4, 5)

        //asFlow 不需要挂起函数修饰符 flowOf自动实现发射元素
        fun asFlowBuilder() = (5..10).asFlow()
        runBlocking {
            flowBuilder().collect(::println)
            flowOfBuilder().collect(::println)
            asFlowBuilder().collect(::println)
        }
        /**
         * Flow取消
         */
        fun flowBuilder2() = flow {
            for (i in 1..10) {
                delay(1000)//可以使用挂起函数
                emit(i)//发射元素
                println("emit is send $i")
            }
        }
        runBlocking {
            withTimeoutOrNull(3500) {
                flowBuilder2().collect(::println)
            }
            println("withTimeoutOrNull collect finish")
        }

//        runBlocking {
//            flowBuilder2().collect { value -> if (value == 3) cancel() }
//            println("cancel collect finish")
//        }
        runBlocking {
            launch {
                (1..5).asFlow().cancellable().collect { value ->
                    println(value)
                    if (value == 3) cancel()
                }
            }
        }

    }
}
