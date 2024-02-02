package cn.chriswood.anthill.example.persistence.other.repository

import cn.chriswood.anthill.example.persistence.other.entity.OtherSysUserEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OtherSysUserRepository : BaseRepository<OtherSysUserEntity, Long>
