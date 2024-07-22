package com.taotao.bmm.persistence.system.repo

import cn.chriswood.anthill.infrastructure.jpa.support.BaseRepository
import com.taotao.bmm.persistence.system.entity.SysUserEntity
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SysUserRepository : BaseRepository<SysUserEntity, Int>,
    QuerydslPredicateExecutor<SysUserEntity>