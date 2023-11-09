package com.example

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Controller("/blockingdemoclient")
class BlockingdemoclientController(
    private val client: BlockingdemoClient,
    private val google: Google,
    private val mdc: MdcDemoClient
) {

    @Get("/suspend/{s}/{times}", produces = ["text/plain"])
    suspend fun waitSuspend(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
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
    suspend fun waitRunBlocking(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
            repeat(times) {
                println("launching #$it")
                launch { client.waitRunBlocking(s, it) }
            }
        }

        return (System.currentTimeMillis() - start).toString()
    }

    @Get("/sleep/{s}/{times}", produces = ["text/plain"])
    suspend fun waitSleep(s: Long, times: Int): String {
        val start = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
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

    @Get("/mdctest/{times}/{wait}", produces = ["text/plain"])
    @Operation(
        tags = ["MDC"]
    )
    suspend fun doMdcTest(
        @PathVariable
        times: Int,
        @PathVariable
        wait: Long
    ): String {
        println("starting test: $times times, wait $wait ms")
        withContext(Dispatchers.IO) {
            repeat(times) {
                launch {
                    val expected = "cpid_$it"
                    val response = mdc.withId(expected, wait)
                    if (response == expected) {
                        println("YES MATCH: $response")
                    } else {
                        println("no match: $response")
                     }
                }
            }
        }
        println("done")
        return "OK"
    }
}