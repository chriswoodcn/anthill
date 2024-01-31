package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAutoImport
import cn.chriswood.anthill.infrastructure.datasource.multi.MultiJpaDataSourceAutoImport
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

class DataSourceAutoImportSelector : ImportSelector {

    @Value("\${anthill.jpa.type}")
    val type: String = DataSourceTypeEnum.MultiJPA.code

    private val mapDataSourceAutoImport = hashMapOf(
        DataSourceTypeEnum.MultiJPA to MultiJpaDataSourceAutoImport::class.java.name,
        DataSourceTypeEnum.DynamicJPA to DynamicDataSourceAutoImport::class.java.name
    )

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        val clz = mapDataSourceAutoImport[DataSourceTypeEnum.getDataSourceTypeByCode(type)]
        return if (clz.isNullOrEmpty()) {
            emptyArray()
        } else arrayOf(clz)
    }
}
