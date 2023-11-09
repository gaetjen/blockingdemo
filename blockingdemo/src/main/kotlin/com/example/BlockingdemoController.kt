package com.example

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Controller("/blockingdemo")
class BlockingdemoController {

    @Get("/suspend/{seconds}/{id}")
    suspend fun waitSuspend(seconds: Long, id: Int): HttpStatus {
        delay(seconds * 1000)
        return HttpStatus.OK
    }

    @Get("/runblocking/{seconds}/{id}")
    fun waitRunBlocking(seconds: Long, id: Int): HttpStatus {
        runBlocking {
            delay(seconds * 1000)
        }
        return HttpStatus.OK
    }

    @Get("/sleep/{seconds}/{id}")
    fun waitSleep(seconds: Long, id: Int): HttpStatus {
        Thread.sleep(seconds * 1000)
        return HttpStatus.OK
    }
}