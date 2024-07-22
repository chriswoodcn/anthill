package com.taotao.bmm.persistence.system.repo

import cn.chriswood.anthill.infrastructure.jpa.support.BaseRepository
import com.taotao.bmm.persistence.system.entity.SysCompanyEntity
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface SysCompanyRepository : BaseRepository<SysCompanyEntity, String>,
    QuerydslPredicateExecutor<SysCompanyEntity>
