package cn.chriswood.anthill.infrastructure.web.captcha

import cn.hutool.captcha.AbstractCaptcha
import cn.hutool.captcha.CircleCaptcha
import cn.hutool.captcha.LineCaptcha
import cn.hutool.captcha.ShearCaptcha

enum class CaptchaCategory(val clazz: Class<out AbstractCaptcha>) {

    /**
     * 线段干扰
     */
    LINE(LineCaptcha::class.java),

    /**
     * 圆圈干扰
     */
    CIRCLE(CircleCaptcha::class.java),

    /**
     * 扭曲干扰
     */
    SHEAR(ShearCaptcha::class.java);
}
