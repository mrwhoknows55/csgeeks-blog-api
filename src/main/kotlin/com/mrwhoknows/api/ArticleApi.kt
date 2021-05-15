package com.mrwhoknows.api

import com.mrwhoknows.model.Article
import com.mrwhoknows.model.ArticleTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception
import java.lang.NumberFormatException

fun Route.article() {
    get("/") {
        call.respond(HttpStatusCode.OK, "Hello, this api is working! \uD83C\uDF89")
    }

    route("/articles") {
        get {
            val orderBy: String? = call.request.queryParameters["orderby"]
            val order: String? = call.request.queryParameters["order"]

            val articles = transaction {
                ArticleTable.selectAll().map {
                    ArticleTable.toArticle(it)
                }
            }
            val isAscending = order.isNullOrBlank() || order == "asc"
            if (isAscending) {
                when (orderBy) {
                    "title" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedBy { it.title })
                    }
                    "author" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedBy { it.author })
                    }
                    "content" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedBy { it.content })
                    }
                    "created" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedBy { it.created })
                    }
                    else -> {
                        call.respond(HttpStatusCode.OK, articles.sortedBy { it.id })
                    }
                }
            } else {
                when (orderBy) {
                    "title" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedByDescending { it.title })
                    }
                    "author" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedByDescending { it.author })
                    }
                    "content" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedByDescending { it.content })
                    }
                    "created" -> {
                        call.respond(HttpStatusCode.OK, articles.sortedByDescending { it.created })
                    }
                    else -> {
                        call.respond(HttpStatusCode.OK, articles.sortedByDescending { it.id })
                    }
                }
            }

        }
    }

    route("/article") {
        get {
            try {
                val id = call.request.queryParameters["id"]?.toInt()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Please Provide Correct ID")
                } else {
                    val article = transaction {
                        ArticleTable.select { ArticleTable.id eq id }.firstOrNull()
                    }
                    if (article != null) {
                        call.respond(HttpStatusCode.OK, ArticleTable.toArticle(article))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "No Article Found")
                    }
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Please Provide Correct ID")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            }
        }

        post {
            val article = call.receive<Article>()

            val id = transaction {
                ArticleTable.insert {
                    it[title] = article.title
                    it[content] = article.content
                    it[author] = article.author
                    it[description] = article.description
                    it[created] = System.currentTimeMillis()
                    it[thumbnail] = article.thumbnail
                    it[tags] = article.tags
                    article.vlink?.let { vlink ->
                        it[ArticleTable.vlink] = vlink
                    }
                }
            }

            call.respond(HttpStatusCode.OK, "Article Added at ${id[ArticleTable.id]}")
        }

        put {
            try {
                val id = call.request.queryParameters["id"]?.toInt()
                val newArticle = call.receive<Article>()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Please Provide Correct ID")
                } else {
                    transaction {
                        ArticleTable.update({ ArticleTable.id eq id }) {
                            it[title] = newArticle.title
                            it[content] = newArticle.content
                            it[author] = newArticle.author
                            it[description] = newArticle.description
                            it[created] = System.currentTimeMillis()
                            it[thumbnail] = newArticle.thumbnail
                            it[tags] = newArticle.tags
                            newArticle.vlink?.let { vlink ->
                                it[ArticleTable.vlink] = vlink
                            }
                        }
                    }
                    call.respond(HttpStatusCode.OK, "Article Updated")
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Please Provide Correct ID")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            }
        }

        delete {
            try {
                val id = call.request.queryParameters["id"]?.toInt()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Please Provide Correct ID")
                } else {
                    transaction {
                        ArticleTable.deleteWhere { ArticleTable.id eq id }
                    }
                    call.respond(HttpStatusCode.BadRequest, "Article Deleted!")
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Please Provide Correct ID")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            }
        }
    }
}

