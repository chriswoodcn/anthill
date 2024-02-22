package cn.chriswood.anthill.infrastructure.web.support

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.hutool.captcha.generator.CodeGenerator
import cn.hutool.core.math.Calculator
import cn.hutool.core.util.CharUtil
import cn.hutool.core.util.RandomUtil
import kotlin.math.max
import kotlin.math.min

class CaptchaUnsignedMathGenerator : CodeGenerator {

    private val numberLength = 2

    private val operators = "+-*"
    private fun getLimit(): Int {
        return ("1" + StringUtil.repeat('0', this.numberLength)).toInt()
    }

    override fun generate(): String {
        val limit: Int = getLimit()
        val a = RandomUtil.randomInt(limit)
        val b = RandomUtil.randomInt(limit)
        var max = max(a.toDouble(), b.toDouble()).toString()
        var min = min(a.toDouble(), b.toDouble()).toString()
        max = StringUtil.rightPad(max, this.numberLength, CharUtil.SPACE)
        min = StringUtil.rightPad(min, this.numberLength, CharUtil.SPACE)

        return max + RandomUtil.randomChar(this.operators) + min + '='
    }

    override fun verify(code: String?, userInputCode: String): Boolean {
        val result: Int = try {
            userInputCode.toInt()
        } catch (e: NumberFormatException) {
            // 用户输入非数字
            return false
        }
        val calculateResult = Calculator.conversion(code).toInt()
        return result == calculateResult
    }

    /**
     * 获取验证码长度
     */
    fun getLength(): Int {
        return numberLength * 2 + 2
    }
}
