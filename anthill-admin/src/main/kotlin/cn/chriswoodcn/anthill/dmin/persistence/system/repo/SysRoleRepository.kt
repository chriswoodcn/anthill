package com.taotao.bmm.persistence.system.repo

import cn.chriswood.anthill.infrastructure.jpa.support.BaseRepository
import com.taotao.bmm.persistence.system.entity.SysRoleEntity
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface SysRoleRepository : BaseRepository<SysRoleEntity, Int>,
    QuerydslPredicateExecutor<SysRoleEntity>
