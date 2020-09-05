package com.mrwhoknows.service

import com.mrwhoknows.model.Articles
import com.mrwhoknows.model.Authors
import com.mrwhoknows.util.Constants
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())

        transaction {
            // create db tables
            SchemaUtils.create(Articles, Authors)

            // initial data in db
//            insertDummyDataInTables()
        }
    }

    private fun insertDummyDataInTables() {
        TODO("Not yet implemented")
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