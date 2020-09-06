package com.mrwhoknows.service

import com.mrwhoknows.model.Articles
import com.mrwhoknows.model.Authors
import com.mrwhoknows.util.Constants
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())

        transaction {
            // create db tables
            SchemaUtils.create(Articles, Authors)

            // initial data in db
//           insertDummyDataInTables()
        }
    }

    private fun insertDummyDataInTables() {
        Articles.insert {
            it[title] = Constants.DUMMY_TITLE
            it[tags] = Constants.DUMMY_TAGS
            it[author] = Constants.DUMMY_AUTHOR
            it[description] = Constants.DUMMY_DESCRIPTION
            it[thumbnail] = Constants.DUMMY_THUMBNAIL
            it[content] = Constants.DUMMY_CONTENT

            it[created] = System.currentTimeMillis()
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = Constants.DATABASE_DRIVER
        config.jdbcUrl = Constants.DATABASE_URI
        config.username = Constants.DATABASE_USER
        config.password = Constants.DATABASE_PASSWORD

//    TODO: understand this and  add/remove if any ...
//    config.maximumPoolSize = 3
//    config.isAutoCommit = false
//    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }
}