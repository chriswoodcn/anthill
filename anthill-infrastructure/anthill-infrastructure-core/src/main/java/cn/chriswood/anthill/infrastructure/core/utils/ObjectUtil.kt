package cn.chriswood.anthill.infrastructure.core.utils

import cn.hutool.core.util.ObjectUtil

object ObjectUtil {
    val isNotNull: (Any?) -> Boolean = ObjectUtil::isNotNull
    val isAllNotEmpty: (Array<Any?>) -> Boolean = ObjectUtil::isAllNotEmpty
    val isAllEmpty: (Array<Any?>) -> Boolean = ObjectUtil::isAllEmpty
    val isEmpty: (Any?) -> Boolean = ObjectUtil::isEmpty
    val isNotEmpty: (Any?) -> Boolean = ObjectUtil::isNotEmpty
    val isNull: (Any?) -> Boolean = ObjectUtil::isNull
}
