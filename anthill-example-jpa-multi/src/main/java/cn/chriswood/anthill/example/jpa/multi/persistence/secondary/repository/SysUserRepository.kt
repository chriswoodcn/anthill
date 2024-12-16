package cn.chriswood.anthill.example.jpa.multi.persistence.secondary.repository

import cn.chriswood.anthill.example.jpa.multi.persistence.secondary.entity.SysUserEntity

interface SysUserRepository : BaseRepository<SysUserEntity, Long>
