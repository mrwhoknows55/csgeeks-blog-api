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
	val created = varchar("created", 255)

	override val primaryKey = PrimaryKey(id)
}

data class Article(
		val author: String,
		val content: String,
		val description: String,
		val tags: String,
		val thumbnail: String,
		val title: String
) {
	val created: String = ""
	val id: Int? = null
}

data class ArticleTags(
		val tags: List<String>
)