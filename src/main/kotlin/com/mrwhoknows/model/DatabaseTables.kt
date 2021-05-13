package com.mrwhoknows.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


object ArticleTable : Table("article") {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id, name = "ArticleTablePK")
    val author = varchar("author", 256)
    val content = text("content")
    val created = long("created")
    val description = text("description")

    //    FIXME: tags should be list
    val tags = text("tags")
    val thumbnail = text("thumbnail")
    val title = text("title")
    val vlink = text("vlink").nullable()


    fun toArticle(row: ResultRow): Article {
        return Article(
            id = row[id],
            title = row[title],
            content = row[content],
            author = row[author],
            description = row[description],
            created = row[created],
            thumbnail = row[thumbnail],
            tags = row[tags],
            vlink = row[vlink],
        )
    }
}

@Serializable
data class Article(
    val id: Int? = null,
    val title: String,
    val content: String,
    val author: String,
    val description: String,
    val created: Long = System.currentTimeMillis(),
    val thumbnail: String,
    val tags: String,
    val vlink: String? = null
)
