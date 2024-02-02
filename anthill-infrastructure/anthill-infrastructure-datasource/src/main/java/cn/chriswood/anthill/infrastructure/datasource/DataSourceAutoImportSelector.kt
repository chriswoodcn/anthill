package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAutoImport
import cn.chriswood.anthill.infrastructure.datasource.multi.MultiJpaDataSourceAutoImport
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

class DataSourceAutoImportSelector : ImportSelector {
    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        return arrayOf(
            DynamicDataSourceAutoImport::class.java.name,
            MultiJpaDataSourceAutoImport::class.java.name
        )
    }
}
