package cn.chriswood.anthill.infrastructure.web.support

import cn.hutool.captcha.generator.CodeGenerator
import cn.hutool.captcha.generator.RandomGenerator

enum class CaptchaType(val clazz: Class<out CodeGenerator>) {
    /**
     * 数字
     */
    MATH(CaptchaUnsignedMathGenerator::class.java),

    /**
     * 字符
     */
    CHAR(RandomGenerator::class.java);
}
