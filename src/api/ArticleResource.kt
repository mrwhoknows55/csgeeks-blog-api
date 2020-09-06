package com.mrwhoknows.api

import com.mrwhoknows.model.Article
import com.mrwhoknows.service.ArticleService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.article(articleService: ArticleService) {

    get("/") {
        call.respond(HttpStatusCode.OK, "Hello, this api is working! \uD83C\uDF89")
    }

    route("/article") {

        get("/{id}") {
            val param = call.parameters["id"]

            try {
                val id = Integer.parseInt(param)
                val article = articleService.getArticleById(id)

                if (article != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        mapOf(
                            "article" to article,
                            "success" to true
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf(
                            "success" to false,
                            "response" to "Article not found"
                        )
                    )
                }

            } catch (e: NumberFormatException) {
                if (param?.toLowerCase().equals("all")) {
                    val articleResponse = articleService.getAllArticles()
                    call.respond(
                        HttpStatusCode.OK,
                        mapOf(
                            "articles" to articleResponse,
                            "success" to true
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("success" to false, "response" to "Error!")
                    )
                }
            }
        }

        post("/") {
            try {
                val article = call.receive<Article>()

                val articleResponse = articleService.saveArticle(article)

                if (articleResponse != null)
                    call.respond(
                        HttpStatusCode.Created,
                        mapOf(
                            "success" to true,
                            "response" to "Article Created Successfully!"
                        )
                    )
                else
                    call.respond(
                        HttpStatusCode.BadGateway,
                        mapOf(
                            "success" to false,
                            "response" to "Server Error Try After Some Time"
                        )
                    )

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.ExpectationFailed,
                    mapOf(
                        "success" to false,
                        "response" to "Error! Please send all fields"
                    )
                )
            }
        }

        put("/{id}") {
            val param = call.parameters["id"]

            try {
                val id = Integer.parseInt(param)
                val article: Article? = call.receive()

                if (article != null) {
                    val articleResponse = articleService.updateArticle(id, article)

                    if (articleResponse != null)
                        call.respond(
                            HttpStatusCode.OK,
                            mapOf(
                                "success" to true,
                                "response" to "Article Updated Successfully!"
                            )
                        )
                    else
                        call.respond(
                            HttpStatusCode.BadGateway,
                            mapOf(
                                "success" to false,
                                "response" to "Server Error Try After Some Time"
                            )
                        )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf(
                            "success" to false,
                            "response" to "Article not found"
                        )
                    )
                }

            } catch (e: NumberFormatException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("success" to false, "response" to "Error!")
                )
            }
        }
    }

    //  TODO make -> only one or more things can be updatable
    delete("{id}") {
        val param = call.parameters["id"]

        try {
            val id = Integer.parseInt(param)
            val isDeleted = articleService.deleteArticle(id)

            if (isDeleted)
                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "success" to true,
                        "response" to "Article Deleted Successfully!"
                    )
                )
            else
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf(
                        "success" to false,
                        "response" to "Article Not Found!"
                    )
                )

        } catch (e: NumberFormatException) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "success" to false,
                    "response" to "Error! Enter Correct Id "
                )
            )
        }
    }
}