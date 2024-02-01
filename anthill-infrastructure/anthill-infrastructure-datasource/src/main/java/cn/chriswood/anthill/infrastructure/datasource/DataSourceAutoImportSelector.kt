package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.common.JpaTypeProperty
import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAutoImport
import cn.chriswood.anthill.infrastructure.datasource.multi.MultiJpaDataSourceAutoImport
import cn.hutool.extra.spring.SpringUtil
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata


class DataSourceAutoImportSelector : ImportSelector {

    private val mapDataSourceAutoImport = hashMapOf(
        DataSourceTypeEnum.MultiJPA to MultiJpaDataSourceAutoImport::class.java.name,
        DataSourceTypeEnum.DynamicJPA to DynamicDataSourceAutoImport::class.java.name
    )

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        return  arrayOf(DynamicDataSourceAutoImport::class.java.name)
    }
}
