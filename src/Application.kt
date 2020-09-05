package com.mrwhoknows

import io.ktor.application.*
import com.fasterxml.jackson.databind.*
import com.mrwhoknows.service.DatabaseFactory
import io.ktor.jackson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    DatabaseFactory.init()
}

