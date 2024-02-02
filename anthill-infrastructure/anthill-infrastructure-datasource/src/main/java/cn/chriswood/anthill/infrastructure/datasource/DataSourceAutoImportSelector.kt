package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAutoImport
import cn.chriswood.anthill.infrastructure.datasource.multi.JpaConfigPrimary
import cn.chriswood.anthill.infrastructure.datasource.multi.JpaConfigSecond
import cn.chriswood.anthill.infrastructure.datasource.multi.JpaConfigThird
import cn.chriswood.anthill.infrastructure.datasource.multi.MultiJpaDataSourceAutoImport
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

class DataSourceAutoImportSelector : ImportSelector {
    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        return arrayOf(
            DynamicDataSourceAutoImport::class.java.name,
        )
    }
}
