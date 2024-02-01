package cn.chriswood.anthill.infrastructure.web.captcha

import cn.hutool.captcha.generator.CodeGenerator
import cn.hutool.captcha.generator.RandomGenerator

enum class CaptchaType(val clazz: Class<out CodeGenerator>) {
    /**
     * 数字
     */
    MATH(UnsignedMathGenerator::class.java),

    /**
     * 字符
     */
    CHAR(RandomGenerator::class.java);
}
