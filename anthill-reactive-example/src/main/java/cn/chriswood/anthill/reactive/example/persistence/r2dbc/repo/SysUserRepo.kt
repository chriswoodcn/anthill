package cn.chriswood.anthill.reactive.example.persistence.r2dbc.repo

import cn.chriswood.anthill.reactive.example.persistence.r2dbc.entity.SysUserEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface SysUserRepo : CoroutineCrudRepository<SysUserEntity, Long> {
    fun findByUsername(username: String): Flow<SysUserEntity>
}
