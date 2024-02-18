package cn.chriswood.anthill.example.persistence.other.repository

import cn.chriswood.anthill.example.persistence.other.entity.OtherSysUserEntity
import org.springframework.stereotype.Repository


@Repository
interface OtherSysUserRepository : BaseRepository<OtherSysUserEntity, Long>
