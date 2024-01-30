package cn.chriswood.anthill.infrastructure.datasource

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

class DataSourceAutoImportSelector : ImportSelector {

    @Value("\${anthill.datasource.type}")
    val type: DataSourceTypeEnum = DataSourceTypeEnum.MultiJPA

    private val mapDataSourceAutoImport = hashMapOf(
        DataSourceTypeEnum.MultiJPA to
            "cn.chriswood.anthill.infrastructure.datasource.MultiJpaDataSourceAutoImport"
    )

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        val clz = mapDataSourceAutoImport[type]
        return if (clz.isNullOrEmpty()) {
            emptyArray()
        } else arrayOf(clz)
    }
}
