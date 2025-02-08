package cn.chriswood.anthill.infrastructure.core.annotation

interface CopyConverter<T> {
    fun convert(source: Any): T?
}
