package com.mrwhoknows

import io.ktor.application.*
import com.fasterxml.jackson.databind.*
import com.mrwhoknows.api.article
import com.mrwhoknows.service.ArticleService
import com.mrwhoknows.service.DatabaseFactory
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    DatabaseFactory.init()

    val articleService = ArticleService()

    install(Routing) {
        article(articleService)
    }
}

