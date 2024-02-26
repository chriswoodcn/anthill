package cn.chriswood.anthill.infrastructure.jpa.support

import cn.chriswood.anthill.infrastructure.jpa.dynamic.DynamicDataSourceAutoImport
import cn.chriswood.anthill.infrastructure.jpa.multi.MultiDataSourceAutoImport
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata

class DataSourceAutoImportSelector : ImportSelector, EnvironmentAware {

    private var environment: Environment? = null

    private val mapImporter: Map<String, Array<String>> = hashMapOf(
        Constants.DYNAMIC to arrayOf(DynamicDataSourceAutoImport::class.java.name),
        Constants.MULTI to arrayOf(MultiDataSourceAutoImport::class.java.name),
    )

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        val binder = Binder.get(environment)
        val dataSourceType = binder.bindOrCreate("anthill.jpa.type", Bindable.of(String::class.java))
        return mapImporter.getOrDefault(dataSourceType, mapImporter[Constants.DYNAMIC]!!)
    }
}
