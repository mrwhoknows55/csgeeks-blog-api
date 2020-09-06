package com.mrwhoknows.service

import com.mrwhoknows.model.Article
import com.mrwhoknows.model.ArticleMeta
import com.mrwhoknows.model.Articles
import com.mrwhoknows.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
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

    suspend fun getArticleById(id: Int): Article? = dbQuery {
        transaction {
            Articles.select {
                Articles.id eq id
            }.mapNotNull { toArticle(it) }.singleOrNull()
        }
    }

    private fun toArticle(row: ResultRow): Article =
        Article(
            title = row[Articles.title],
            author = row[Articles.author],
            content = row[Articles.content],
            tags = row[Articles.tags],
            description = row[Articles.description],
            created = row[Articles.created],
            id = row[Articles.id],
            thumbnail = row[Articles.thumbnail]
        )

    suspend fun saveArticle(article: Article): Article? {
        var key = 0
        dbQuery {
            key = (Articles.insert {
                it[author] = article.author
                it[title] = article.title
                it[thumbnail] = article.thumbnail
                it[tags] = article.tags
                it[description] = article.description
                it[created] = System.currentTimeMillis()
                it[content] = article.content
            } get Articles.id)
        }
        return getArticleById(key)
    }

    suspend fun deleteArticle(id: Int): Boolean {
        return dbQuery {
            Articles.deleteWhere { Articles.id eq id } > 0
        }
    }
}