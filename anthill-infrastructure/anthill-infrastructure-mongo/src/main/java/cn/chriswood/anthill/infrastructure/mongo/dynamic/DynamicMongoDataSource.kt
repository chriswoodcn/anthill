package cn.chriswood.anthill.infrastructure.mongo.dynamic

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DynamicMongoDataSource(val value: String)


