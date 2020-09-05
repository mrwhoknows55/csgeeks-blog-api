package com.mrwhoknows.service

import com.mrwhoknows.model.ArticleMeta
import com.mrwhoknows.model.Articles
import com.mrwhoknows.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ArticleService {

    suspend fun getAllArticles(): List<ArticleMeta> = dbQuery {
        transaction {
            Articles.selectAll().mapNotNull { toArticleMeta(it) }
        }
    }

    private fun toArticleMeta(row: ResultRow): ArticleMeta =
        ArticleMeta(
            id = row[Articles.id],
            author = row[Articles.author],
            title = row[Articles.title],
            thumbnail = row[Articles.thumbnail],
            tags = row[Articles.tags],
            description = row[Articles.description],
            created = row[Articles.created],
        )
}