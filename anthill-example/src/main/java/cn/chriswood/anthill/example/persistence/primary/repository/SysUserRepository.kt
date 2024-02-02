package cn.chriswood.anthill.example.persistence.primary.repository

import cn.chriswood.anthill.example.persistence.primary.entity.SysUserEntity
import org.springframework.stereotype.Repository

@Repository
interface SysUserRepository : BaseRepository<SysUserEntity, Long>
