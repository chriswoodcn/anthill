package cn.chriswood.anthill.infrastructure.web.config

import cn.chriswood.anthill.infrastructure.web.captcha.CaptchaProperties
import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.CircleCaptcha
import cn.hutool.captcha.LineCaptcha
import cn.hutool.captcha.ShearCaptcha
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import java.awt.Color
import java.awt.Font

@AutoConfiguration
@EnableConfigurationProperties(CaptchaProperties::class)
@ConditionalOnProperty(
    prefix = "anthill.web.captcha",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class CaptchaConfig {
    private val width = 160
    private val height = 60
    private val background = Color.PINK
    private val font = Font("arial", Font.BOLD, 48)

    /**
     * 圆圈干扰验证码
     */
    @Lazy
    @Bean
    fun circleCaptcha(): CircleCaptcha {
        val captcha = CaptchaUtil.createCircleCaptcha(width, height)
        captcha.setBackground(background)
        captcha.setFont(font)
        return captcha
    }

    /**
     * 线段干扰的验证码
     */
    @Lazy
    @Bean
    fun lineCaptcha(): LineCaptcha {
        val captcha = CaptchaUtil.createLineCaptcha(width, height)
        captcha.setBackground(background)
        captcha.setFont(font)
        return captcha
    }

    /**
     * 扭曲干扰验证码
     */
    @Lazy
    @Bean
    fun shearCaptcha(): ShearCaptcha {
        val captcha = CaptchaUtil.createShearCaptcha(width, height)
        captcha.setBackground(background)
        captcha.setFont(font)
        return captcha
    }
}
