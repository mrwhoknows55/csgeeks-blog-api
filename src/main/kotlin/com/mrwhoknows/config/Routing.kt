package com.mrwhoknows.config

import com.mrwhoknows.api.article
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun Application.setupConfiguration() {
    //     Content Negotiation
    install(ContentNegotiation) {
        json()
    }

    //    Routes
    install(Routing){
        article()
    }
}
