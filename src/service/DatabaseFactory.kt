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
            it[title] = "dummy title 2"
            it[tags] = "dummy,tags2"
            it[author] = "dummy author2"
            it[description] = "dummy description 2"
            it[thumbnail] = "dummy thumb link2"
            it[created] = System.currentTimeMillis()
            it[content] = "dummy content 2"
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = Constants.databaseDriver
        config.jdbcUrl = Constants.databaseURI
        config.username = Constants.databaseUser
        config.password = Constants.databasePassword

//    TODO: understand this and  add/remove if any ...
//    config.maximumPoolSize = 3
//    config.isAutoCommit = false
//    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.validate()
        return HikariDataSource(config)
    }

    // TODO: examine ->
    suspend fun <T> dbQuery(
        block: suspend () -> T
    ): T =
        newSuspendedTransaction { block() }
}