package com.mrwhoknows

import com.mrwhoknows.config.DatabaseFactory
import com.mrwhoknows.config.DatabaseFactory.dbQuery
import com.mrwhoknows.config.setupConfiguration
import com.mrwhoknows.model.ArticleTable
import io.ktor.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    setupConfiguration()
    DatabaseFactory.init()
}
