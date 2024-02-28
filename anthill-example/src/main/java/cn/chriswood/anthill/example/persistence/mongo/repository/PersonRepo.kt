package cn.chriswood.anthill.example.persistence.mongo.repository

import cn.chriswood.anthill.example.persistence.mongo.entity.Person
import cn.chriswood.anthill.infrastructure.mongo.support.BaseRepository

interface PersonRepo : BaseRepository<Person, String> {
    fun findByUserId(userId: Long): Person
}
