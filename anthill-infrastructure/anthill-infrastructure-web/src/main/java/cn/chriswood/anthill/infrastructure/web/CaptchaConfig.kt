package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.web.support.CaptchaProperties
import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.CircleCaptcha
import cn.hutool.captcha.LineCaptcha
import cn.hutool.captcha.ShearCaptcha
import org.slf4j.LoggerFactory
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
    private val log = LoggerFactory.getLogger(javaClass)

    private val width = 160
    private val height = 60
    private val background = Color.PINK
    private val font = Font("arial", Font.BOLD, 48)

    /**
     * 圆圈干扰验证码
     */
    @Bean
    @Lazy
    fun circleCaptcha(): CircleCaptcha {
        val captcha = CaptchaUtil.createCircleCaptcha(width, height)
        captcha.setBackground(background)
        captcha.setFont(font)
        log.debug(">>>>>>>>>> init CaptchaConfig circleCaptcha >>>>>>>>>>")
        return captcha
    }

    /**
     * 线段干扰的验证码
     */
    @Bean
    @Lazy
    fun lineCaptcha(): LineCaptcha {
        val captcha = CaptchaUtil.createLineCaptcha(width, height)
        captcha.setBackground(background)
        captcha.setFont(font)
        log.debug(">>>>>>>>>> init CaptchaConfig lineCaptcha >>>>>>>>>>")
        return captcha
    }

    /**
     * 扭曲干扰验证码
     */
    @Bean
    @Lazy
    fun shearCaptcha(): ShearCaptcha {
        val captcha = CaptchaUtil.createShearCaptcha(width, height)
        captcha.setBackground(background)
        captcha.setFont(font)
        log.debug(">>>>>>>>>> init CaptchaConfig shearCaptcha >>>>>>>>>>")
        return captcha
    }
}
