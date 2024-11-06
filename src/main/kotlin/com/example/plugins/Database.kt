package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import io.ktor.server.html.*
import io.ktor.server.http.content.*
import kotlinx.html.*

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    transaction {
        SchemaUtils.create(Quotes)
    }
    val quoteService = QuoteService() 
    routing {
      get("/quotes-htmx") {
        val quotes = quoteService.list()    
        call.respondHtml {
          head {
            script(src = "https://unpkg.com/htmx.org@1.9.6") {} 
          }
        body {
          h1 { +"Quotes (HTMX)" }
          div {
            id = "quotes-list"
            quotes.forEach { quote ->
              div {
                p { +quote.quote }
                p { +"― ${quote.author}" }
              }
            }
          }
          form(method = FormMethod.post, action = "/quotes", encType = FormEncType.applicationXWwwFormUrlEncoded) {
            attributes["hx-post"] = "/quotes"
            attributes["hx-target"] = "#quotes-list"
            attributes["hx-swap"] = "beforeend" 
            div {
              label { +"Quote:" }
              textInput(name = "quote")
            }
            div {
              label { +"Author:" }
              textInput(name = "author")
            }
            button(type = ButtonType.submit) { +"Add Quote" }
          }
        }
      }
    }
    post("/quotes") {
      val parameters = call.receiveParameters()
      val quote = parameters["quote"] ?: ""
      val author = parameters["author"] ?: ""
      val newQuote = Quote(quote = quote, author = author)
      val id = quoteService.create(newQuote)
      val createdQuote = quoteService.read(id) 
      //call.respondHtml(HttpStatusCode.Created) { 
      call.respondHtmlFragment(HttpStatusCode.Created) {
        body{
        div {
          p { +createdQuote.quote }
          p { +"― ${createdQuote.author}" }
        }
}
      }
    }
   /* post("/quotes") {
        val parameters = call.receiveParameters()
          val quote = parameters["quote"] ?: ""
          val author = parameters["author"] ?: ""
 
          val newQuote = Quote(quote = quote, author = author) 
 
          val id = quoteService.create(newQuote)
          call.respond(HttpStatusCode.Created, id)
        }*/
        get("/quotes") {
            val quotes = quoteService.list()
            call.respond(HttpStatusCode.OK, quotes)
        }
    }
}
