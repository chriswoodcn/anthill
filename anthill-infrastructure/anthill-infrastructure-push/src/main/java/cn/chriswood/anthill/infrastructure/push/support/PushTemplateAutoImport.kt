package cn.chriswood.anthill.infrastructure.push.support

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata

class PushTemplateAutoImport : ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val MAIL_ACCOUNT_PREFIX = "anthill.push"

    private val BEAN_NAME = "%sPushTemplate"

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
        val mailPropertiesBinder = binder.bind(MAIL_ACCOUNT_PREFIX, PushProperties::class.java)
        if (mailPropertiesBinder.isBound) {
            mailPropertiesBinder.get().accounts?.forEach {
                injectMailAccountBeanByKey(it.key, it, registry)
            }
        }
    }

    private fun injectMailAccountBeanByKey(
        key: String,
        it: PushTemplateProperty,
        registry: BeanDefinitionRegistry
    ) {
        if (it.supplier.isBlank() || it.key.isBlank()) return
        val templateBeanDefinition = BeanDefinitionBuilder
            .genericBeanDefinition(PushTemplate::class.java) {
                var pushTemplate: PushTemplate? = null
                when (it.supplier) {
                    "jpush" -> {
                        pushTemplate = JPushPushTemplate(it)
                    }
                }
                pushTemplate
            }.setScope(BeanDefinition.SCOPE_PROTOTYPE).getBeanDefinition()
        log.debug(">>>>>>>>>> registry pushTemplate $key >>>>>>>>>>")
        registry.registerBeanDefinition(
            String.format(BEAN_NAME, "${it.supplier}${it.key}"),
            templateBeanDefinition
        )
    }
}
