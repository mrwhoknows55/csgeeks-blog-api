package com.mrwhoknows

import com.mrwhoknows.config.setupConfiguration
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({  setupConfiguration()}) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello, this api is working! \uD83C\uDF89", response.content)
            }
        }
    }
}