package cn.chriswood.anthill.infrastructure.mail

import cn.chriswood.anthill.infrastructure.mail.support.MailProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@AutoConfiguration
@EnableConfigurationProperties(MailProperties::class)
class MailConfig
