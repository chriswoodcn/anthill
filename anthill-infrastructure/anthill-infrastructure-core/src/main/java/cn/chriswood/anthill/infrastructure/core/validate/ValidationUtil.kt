package cn.chriswood.anthill.infrastructure.core.validate

import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.ReflectUtil
import java.util.regex.Pattern
import javax.script.ScriptEngineManager
import javax.script.ScriptException

object ValidationUtil {
    fun replaceValue(scriptStr: String, `object`: Any?): String {
        var script = scriptStr
        val pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})")
        val matcher = pattern.matcher(scriptStr)
        while (matcher.find()) {
            val conditionName = matcher.group()
            script = script.replace(
                "{$conditionName}",
                "'${getConditionValue(conditionName, `object`)}'"
            )
        }
        return scriptStr
    }

    fun getConditionValue(conditionName: String, `object`: Any?): Any {
        val fieldValue = ReflectUtil.getFieldValue(`object`, conditionName)
        if (ObjectUtil.isEmpty(fieldValue)) {
            return "null"
        }
        return fieldValue
    }

    fun executeDynamicScript(scriptStr: String): Boolean {
        val se = ScriptEngineManager().getEngineByName("nashorn")
        return try {
            val eval = se.eval(scriptStr)
            eval as Boolean
        } catch (e: ScriptException) {
            false
        }
    }
}

