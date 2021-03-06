package com.mrwhoknows.model

import org.jetbrains.exposed.sql.Table

object Articles : Table() {
    val id = integer("id").autoIncrement()
    val author = varchar("author", 255)
    val title = text("title")
    val content = text("content")
    val description = varchar("description", 255)
    val tags = text("tags")
    val thumbnail = varchar("thumbnail", 255)
    val created = long("created")

    override val primaryKey = PrimaryKey(id)
}

data class Article(
    val id: Int? = null,
    val title: String,
    val author: String,
    val created: Long? = null,
    val thumbnail: String,
    val description: String,
    val content: String,
    val tags: String
)

data class ArticleMeta(
    val id: Int,
    val title: String,
    val description: String,
    val author: String,
    val created: Long,
    val thumbnail: String,
    val tags: String
)

data class ArticleTags(
    val tags: List<String>
)