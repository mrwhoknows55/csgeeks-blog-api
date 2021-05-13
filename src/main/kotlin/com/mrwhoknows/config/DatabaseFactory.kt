package com.mrwhoknows.config

import com.mrwhoknows.model.ArticleTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object DatabaseFactory {

    fun init() {
        Database.connect(hikariConfig())
        transaction {
            SchemaUtils.create(ArticleTable)
        }
    }

    private const val DATABASE_USER = "postgres"
    private const val DATABASE_PASSWORD = "password"

    private fun hikariConfig(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            username = DATABASE_USER
            password = DATABASE_PASSWORD
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }

    suspend fun drop() {
        dbQuery {
            SchemaUtils.drop(ArticleTable)
        }
    }
}