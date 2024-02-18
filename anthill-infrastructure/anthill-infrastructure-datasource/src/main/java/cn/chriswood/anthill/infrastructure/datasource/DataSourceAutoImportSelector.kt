package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.common.Constants
import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAutoImport
import cn.chriswood.anthill.infrastructure.datasource.multi.MultiJpaDataSourceAutoImport
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata

class DataSourceAutoImportSelector : ImportSelector, EnvironmentAware {

    private var environment: Environment? = null

    private val mapImporter: Map<String, String> = hashMapOf(
        Constants.DYNAMIC to DynamicDataSourceAutoImport::class.java.name,
        Constants.MULTI to MultiJpaDataSourceAutoImport::class.java.name,
    )

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        val binder = Binder.get(environment)
        val dataSourceType = binder.bindOrCreate("anthill.jpa.type", Bindable.of(String::class.java))
        return arrayOf(mapImporter.getOrDefault(dataSourceType, mapImporter[Constants.MULTI]!!))
    }
}
