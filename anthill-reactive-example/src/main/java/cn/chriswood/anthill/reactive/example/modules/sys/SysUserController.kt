package cn.chriswood.anthill.reactive.example.modules.sys

import cn.chriswood.anthill.infrastructure.web.base.R
import cn.chriswood.anthill.reactive.example.persistence.r2dbc.entity.SysUserEntity
import cn.chriswood.anthill.reactive.example.persistence.r2dbc.repo.SysUserRepo
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/sys/user")
@Validated
class SysUserController(
    private val repo: SysUserRepo
) {
    @GetMapping("/listAll")
    suspend fun listAll(): Mono<R<List<SysUserEntity>>> {
        val asFlux = repo.findAll()
            .asFlux()
            .collectList()
        return Mono.just(R.ok(asFlux.awaitSingle()))
    }
}
