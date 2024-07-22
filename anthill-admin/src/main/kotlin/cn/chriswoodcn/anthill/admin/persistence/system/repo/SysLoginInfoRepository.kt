package com.taotao.bmm.persistence.system.repo

import cn.chriswood.anthill.infrastructure.jpa.support.BaseRepository
import com.taotao.bmm.persistence.system.entity.SysLoginInfoEntity
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface SysLoginInfoRepository : BaseRepository<SysLoginInfoEntity, Long>,
    QuerydslPredicateExecutor<SysLoginInfoEntity>