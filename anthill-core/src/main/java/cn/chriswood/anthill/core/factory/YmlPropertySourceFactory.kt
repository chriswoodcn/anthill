package cn.chriswood.anthill.core.factory

import cn.chriswood.anthill.core.util.StringUtil
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.DefaultPropertySourceFactory
import org.springframework.core.io.support.EncodedResource

class YmlPropertySourceFactory : DefaultPropertySourceFactory() {

    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val sourceName = resource.resource.filename
        if (StringUtil.isNotBlank(sourceName) && StringUtil.endsWithAny(sourceName, arrayOf(".yml", ".yaml"))) {
            val factory = YamlPropertiesFactoryBean()
            factory.setResources(resource.resource)
            factory.afterPropertiesSet()
            return PropertiesPropertySource(sourceName!!, factory.getObject()!!)
        }
        return super.createPropertySource(name, resource)
    }
}
