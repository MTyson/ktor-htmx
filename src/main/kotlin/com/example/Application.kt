package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
//import io.ktor.serialization.kotlinx.json.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  //install(ContentNegotiation) {
  //  json() // Configure the plugin to use kotlinx.serialization for JSON
  //}

  configureTemplating()
  //configureRouting()
  install(RequestLoggingPlugin)

  configureDatabases()

  //configureMonitoring()
  //install(RequestLoggingPlugin)
}
val RequestLoggingPlugin = createApplicationPlugin(name = "RequestLoggingPlugin") {
    onCall { call ->
        //println("FOOOO : " + call.request.local.scheme)
        val scheme = call.request.local.scheme 
        val host = call.request.local.localHost 
        val port = call.request.local.localPort
        val uri = call.request.local.uri

        println("Request URL: $scheme://$host:$port$uri")
    }
}
/*
val AllRequestsLoggingPlugin = createApplicationPlugin(name = "AllRequestsLoggingPlugin") {
    onCall { call ->
        // Access information about the request
        //val method = call.request.method.value
        //val uri = call.request.uri
        //val headers = call.request.headers.entries().joinToString { "${it.key}: ${it.value}" }

        // Log the request details
        println("Incoming request:")
        //println("Method: $method")
        //println("URI: $uri")
        //println("Headers: $headers")
        //println("------------------")
    }
}
*/
/*val RequestLoggingPlugin = createApplicationPlugin(name = "RequestLoggingPlugin") {
    onCall { call ->
        call.request.origin.apply {
            println("Request URL: $scheme://$localHost:$localPort$uri")
        }
    }
}*/
