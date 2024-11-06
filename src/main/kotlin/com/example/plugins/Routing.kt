package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.html.dom.*
import io.ktor.http.*  // Import for HttpStatusCode
import io.ktor.server.request.*  // Import for receiveParameters

fun Application.configureRouting() {
    val quotes = mutableListOf(
        "This Mind is the matrix of all matter." to "Max Planck",
        "All religions, arts and sciences are branches of the same tree." to "Albert Einstein",
        "The mind is everything. What you think you become." to "Buddha"
    )
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title("Quotes")
                }
                body {
                    h1 { +"Quotes to Live By" }
                    ul {
                        quotes.forEach { (quote, author) ->
                            li {
                                p { +quote }
                                p { +"― $author" }
                            }
                        }
                    }
                }
            }
        }

        get("/quotes/{author?}") {
            val author = call.parameters["author"]
            val filteredQuotes = if (author != null) {
                quotes.filter { it.second.equals(author, ignoreCase = true) }
            } else {
                quotes
            }

            call.respondHtml {
                head { title("Quotes") }
                body {
                    h1 { +"Quotes to Live By" }
                    ul {
                        filteredQuotes.forEach { (quote, author) ->
                            li {
                                p { +quote }
                                p { +"― $author" }
                            }
                        }
                    }
                }
            }
        }

        post("/quotes") {
            val formParameters = call.receiveParameters()
            val quote = formParameters["quote"] ?: ""
            val author = formParameters["author"] ?: ""
            
            // Add the new quote to the list
            quotes.add(quote to author)

            call.respondRedirect("/quotes") 
        }

        get("/add-quote") { // New route to display the form
            call.respondHtml {
                body {
                    form(method = FormMethod.post, action = "/quotes") { // Form with POST method
                        label { +"Quote:" }
                        br {}
                        textInput(name = "quote") { } // Input field for the quote
                        br {}
                        label { +"Author:" }
                        br {}
                        textInput(name = "author") { } // Input field for the author
                        br {}
                        submitInput { +"Add Quote" } // Submit button
                    }
                }
            }
        }
    }
}
