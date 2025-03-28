package cn.chriswood.anthill.example.jpa.multi.persistence.secondary.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<T, ID> : JpaSpecificationExecutor<T>, JpaRepository<T, ID>
