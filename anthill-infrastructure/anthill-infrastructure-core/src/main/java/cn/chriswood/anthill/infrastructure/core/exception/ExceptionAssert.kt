package cn.chriswood.anthill.infrastructure.core.exception

import cn.hutool.core.util.ObjectUtil

interface ExceptionAssert {
    fun eject(vararg args: Any?)
    fun eject(vararg args: Any?, callback: () -> Unit) {
        callback()
        eject(*args)
    }

    fun notNull(o: Any?, vararg args: Any?) {
        stateTrue(ObjectUtil.isNotNull(o), *args)
    }

    fun notNull(o: Any?, vararg args: Any?, callback: () -> Unit) {
        stateTrue(ObjectUtil.isNotNull(o), *args, callback)
    }

    fun isNull(o: Any?, vararg args: Any?) {
        stateTrue(ObjectUtil.isNull(o), *args)
    }

    fun isNull(o: Any?, vararg args: Any?, callback: () -> Unit?) {
        stateTrue(ObjectUtil.isNull(o), *args, callback)
    }

    fun notEmpty(s: String?, vararg args: Any?) {
        stateTrue(!s.isNullOrEmpty(), *args)
    }

    fun notEmpty(s: String?, vararg args: Any?, callback: () -> Unit?) {
        stateTrue(!s.isNullOrEmpty(), *args, callback)
    }

    fun isEmpty(s: String?, vararg args: Any?) {
        stateTrue(s.isNullOrEmpty(), *args)
    }

    fun isEmpty(s: String?, vararg args: Any?, callback: () -> Unit?) {
        stateTrue(s.isNullOrEmpty(), *args, callback)
    }

    fun notEmpty(c: Collection<*>, vararg args: Any?) {
        stateTrue(!c.isEmpty(), *args)
    }

    fun notEmpty(c: Collection<*>, vararg args: Any?, callback: () -> Unit) {
        stateTrue(!c.isEmpty(), *args, callback)
    }

    fun isEmpty(c: Collection<*>, vararg args: Any?) {
        stateTrue(c.isEmpty(), *args)
    }

    fun isEmpty(c: Collection<*>, vararg args: Any?, callback: () -> Unit?) {
        stateTrue(c.isEmpty(), *args, callback)
    }

    fun stateTrue(expression: Boolean, vararg args: Any?)

    fun stateTrue(expression: Boolean, vararg args: Any?, callback: () -> Unit?)

    fun stateFalse(expression: Boolean, vararg args: Any?) {
        stateTrue(!expression, *args)
    }

    fun stateFalse(expression: Boolean, vararg args: Any?, callback: () -> Unit?) {
        stateTrue(!expression, *args, callback)
    }
}
