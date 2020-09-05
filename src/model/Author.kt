package com.mrwhoknows.model

import org.jetbrains.exposed.sql.Table

object Authors : Table() {
	val authId = integer("authId").autoIncrement()
	val name = varchar("name", 255)
	val bio = text("bio")
	val mail = varchar("mail", 255)
	//TODO : add socials

	override val primaryKey = PrimaryKey(authId)
}

data class Author(
		val authId: Int,
		val bio: String,
		val mail: String,
		val name: String,
		val social: List<Social>
) {
	data class Social(
			val name: String,
			val url: String
	)
}