package com.example

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Controller("/blockingdemoclient")
class BlockingdemoclientController(
    private val client: BlockingdemoClient,
    private val google: Google,
) {

    @Get("/suspend/{s}/{times}", produces = ["text/plain"])
    fun waitSuspend(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        runBlocking {
            repeat(times) {
                println("launching #$it")
                launch {
                    client.waitSuspend(s, it)
                }
            }
        }
        return (System.currentTimeMillis() - start).toString()
    }

    @Get("/sequential/{s}/{times}", produces = ["text/plain"])
    fun waitSequential(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        repeat(times) {
            println("launching #$it")
            client.waitSuspendBlocking(s, it)
        }
        return (System.currentTimeMillis() - start).toString()
    }

    @Get("/runblocking/{s}/{times}", produces = ["text/plain"])
    fun waitRunBlocking(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        runBlocking {
            withContext(Dispatchers.IO) {
                repeat(times) {
                    println("launching #$it")
                    launch { client.waitRunBlocking(s, it) }
                }
            }
        }
        return (System.currentTimeMillis() - start).toString()
    }

    @Get("/sleep/{s}/{times}", produces = ["text/plain"])
    fun waitSleep(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        runBlocking {
            repeat(times) {
                println("launching #$it")
                launch { client.waitSleep(s, it) }
            }
        }
        return (System.currentTimeMillis() - start).toString()
    }

    @Get("/google/{times}")
    suspend fun google(times: Int): String {
        val start = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
            repeat(times) {
                println("launching #$it")
                launch { google.index() }
            }
        }

        return (System.currentTimeMillis() - start).toString()
    }
}