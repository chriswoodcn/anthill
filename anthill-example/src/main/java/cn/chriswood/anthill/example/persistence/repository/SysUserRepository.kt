package cn.chriswood.anthill.example.persistence.repository

import cn.chriswood.anthill.example.persistence.entity.SysUserEntity
import org.springframework.stereotype.Repository

@Repository
interface SysUserRepository : BaseRepository<SysUserEntity, String> {
}
