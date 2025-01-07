package cn.chriswood.anthill.infrastructure.core

import org.slf4j.LoggerFactory

// 定义一个内联函数来获取日志记录器
inline fun <reified T : Any> T.logger(): org.slf4j.Logger =
    LoggerFactory.getLogger(T::class.java)
