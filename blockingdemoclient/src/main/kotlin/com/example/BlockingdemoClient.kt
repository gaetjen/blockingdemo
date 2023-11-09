package com.example

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Head
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("server")
interface BlockingdemoClient {
    @Get("/suspend/{s}/{id}")
    suspend fun waitSuspend(s: Long, id: Int): HttpStatus


    @Get("/suspend/{s}/{id}")
    fun waitSuspendBlocking(s: Long, id: Int): HttpStatus

    @Get("/runblocking/{s}/{id}")
    suspend fun waitRunBlocking(s: Long, id: Int): HttpStatus


    @Get("/sleep/{s}/{id}")
    suspend fun waitSleep(s: Long, id: Int): HttpStatus
}

@Client("https://www.google.com")
interface Google {
    @Get("/")
    suspend fun index(): String
}

@Client("mdc")
interface MdcDemoClient {
    @Get("/last")
    suspend fun last(): String

    @Get("/no-delay/{identity}")
    suspend fun noDelay(@PathVariable identity: String): String

    @Get(
        "/with-id/{identity}/{wait}"
    )
    suspend fun withId(
        @PathVariable identity: String,
        @PathVariable wait: Long,
        @Header("Accept") accept: String = "text/plain"
    ): String
}