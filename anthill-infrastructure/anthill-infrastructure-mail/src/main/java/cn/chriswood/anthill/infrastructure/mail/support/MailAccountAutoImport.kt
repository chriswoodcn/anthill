package cn.chriswood.anthill.infrastructure.mail.support

import cn.hutool.core.util.StrUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata

class MailAccountAutoImport : ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val MAIL_ACCPUNT_PREFIX = "anthill.mail.account"

    private val BEAN_NAME = "%sMailAccount"

    private var environment: Environment? = null

    private var context: ApplicationContext? = null
    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun setApplicationContext(context: ApplicationContext) {
        this.context = context
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        log.debug(">>>>>>>>>> init MailAccount >>>>>>>>>>")
        val binder = Binder.get(environment)
        val accountPropertiesList =
            binder.bindOrCreate(MAIL_ACCPUNT_PREFIX, Bindable.listOf(MailAccountProperties::class.java))
        if (!accountPropertiesList.isNullOrEmpty()) {
            accountPropertiesList.forEach {
                injectMailAccountBean(it, registry)
            }
        }
    }

    private fun injectMailAccountBean(it: MailAccountProperties?, registry: BeanDefinitionRegistry) {
        if (it == null || it.user.isEmpty()) return
        val mailAccountBeanDefinition = BeanDefinitionBuilder
            .genericBeanDefinition(EnhanceMailAccount::class.java) {
                val account = MailAccount()
                account.setHost(it.host)
                account.setPort(it.port)
                account.setAuth(it.auth)
                account.setFrom(it.from)
                account.setUser(it.user)
                account.setPass(it.pass)
                account.setSocketFactoryPort(it.port)
                account.setStarttlsEnable(it.starttlsEnable)
                account.setSslEnable(it.sslEnable)
                account.setTimeout(it.timeout)
                account.setConnectionTimeout(it.connectionTimeout)
                val enhanceMailAccount = EnhanceMailAccount(account)
                if (it.limitCount>0) enhanceMailAccount.limitCount = it.limitCount
                enhanceMailAccount
            }.setScope(BeanDefinition.SCOPE_PROTOTYPE).getBeanDefinition()
        val subPre = StrUtil.subPre(it.from, it.from.indexOf('@') + 1)
        log.debug(">>>>>>>>>> registry EnhanceMailAccount  ${it.user} >>>>>>>>>>")
        registry.registerBeanDefinition(
            String.format(BEAN_NAME, subPre),
            mailAccountBeanDefinition
        )
    }
}
