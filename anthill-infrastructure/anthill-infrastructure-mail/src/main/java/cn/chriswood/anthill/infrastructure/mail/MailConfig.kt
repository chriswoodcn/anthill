package cn.chriswood.anthill.infrastructure.mail

import cn.chriswood.anthill.infrastructure.mail.support.MailAccountAutoImport
import cn.chriswood.anthill.infrastructure.mail.support.MailProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.mail",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableConfigurationProperties(MailProperties::class)
@Import(MailAccountAutoImport::class)
class MailConfig
