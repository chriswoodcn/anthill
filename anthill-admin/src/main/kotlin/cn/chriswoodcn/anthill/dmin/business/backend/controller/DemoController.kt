package com.taotao.bmm.business.backend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.OutputStream
import java.util.*
import java.util.concurrent.Executors


@RestController
@RequestMapping("/backend/demo")
@Tag(name = "v1-backend-demo接口")
class DemoController {

    private val executor =
        Executors.newCachedThreadPool()

    @GetMapping("/rbe")
    fun handleRbe(): ResponseEntity<ResponseBodyEmitter> {
        val emitter = ResponseBodyEmitter()
        executor.execute {
            try {
                emitter.send(
                    "/rbe" + " @ " + Date(), MediaType.TEXT_PLAIN
                )
                emitter.complete()
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }
        return ResponseEntity(emitter, HttpStatus.OK)
    }

    private val nonBlockingService = Executors
        .newCachedThreadPool()

    @GetMapping("/sse")
    fun handleSse(): SseEmitter {
        val emitter = SseEmitter()
        nonBlockingService.execute {
            try {
                emitter.send("/sse" + " @ " + Date())
                // we could send more events
                emitter.complete()
            } catch (ex: java.lang.Exception) {
                emitter.completeWithError(ex)
            }
        }
        return emitter
    }

    @GetMapping("/srb")
    fun handleSrb(): ResponseEntity<StreamingResponseBody?> {
        val stream = StreamingResponseBody { out: OutputStream ->
            val msg = "/srb" + " @ " + Date()
            out.write(msg.toByteArray())
        }
        return ResponseEntity(stream, HttpStatus.OK)
    }
}