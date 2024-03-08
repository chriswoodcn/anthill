package cn.chriswood.anthill.reactive.example.persistence.r2dbc.repo

import cn.chriswood.anthill.reactive.example.persistence.r2dbc.entity.SysUserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

interface SysUserRepo : CoroutineSortingRepository<SysUserEntity, Long>, CoroutineCrudRepository<SysUserEntity, Long>
