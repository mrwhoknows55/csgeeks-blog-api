package com.mrwhoknows.api

import com.mrwhoknows.model.Article
import com.mrwhoknows.model.ArticleTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception
import java.lang.NumberFormatException

fun Route.article() {
    get("/") {
        call.respond(HttpStatusCode.OK, "Hello, this api is working! \uD83C\uDF89")
    }

    route("/posts") {
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

    route("/post") {
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
    }
}

