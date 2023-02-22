package com.example

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Controller("/blockingdemo")
class BlockingdemoController {

    @Get("/suspend/{s}/{id}")
    suspend fun waitSuspend(s: Long, id: Int): HttpStatus {
        delay(s * 1000)
        return HttpStatus.OK
    }

    @Get("/runblocking/{s}/{id}")
    fun waitRunBlocking(s: Long, id: Int): HttpStatus {
        runBlocking {
            delay(s * 1000)
        }
        return HttpStatus.OK
    }

    @Get("/sleep/{s}/{id}")
    fun waitSleep(s: Long, id: Int): HttpStatus {
        Thread.sleep(s * 1000)
        return HttpStatus.OK
    }
}