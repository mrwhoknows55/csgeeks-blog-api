package com.mrwhoknows.api

import com.mrwhoknows.service.ArticleService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.article(articleService: ArticleService) {

    get("/") {
        call.respond("Hello, this api is working! \uD83C\uDF89")
    }

    route("/articles") {
        get("/") {
            call.respond(articleService.getAllArticles())
        }
    }

}