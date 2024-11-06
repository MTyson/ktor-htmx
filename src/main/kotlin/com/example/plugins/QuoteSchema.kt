// QuoteSchema.kt

package com.example.plugins

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Quotes : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val quote = text("quote")
    val author = text("author")

    override val primaryKey = PrimaryKey(id, name = "PK_Quotes_ID")
}
data class Quote(val id: Int? = null, val quote: String, val author: String)
class QuoteService {
    suspend fun create(quote: Quote): Int = withContext(Dispatchers.IO) {
      println("BEGIN create: " + quote)
      transaction {
        Quotes.insert {
          it[this.quote] = quote.quote
          it[this.author] = quote.author
        } get Quotes.id
      } ?: throw Exception("Unable to create quote")
    }
    suspend fun read(id: Int): Quote = withContext(Dispatchers.IO) {
      transaction {
        Quotes.select { Quotes.id eq id }
          .map {
            Quote(
              id = it[Quotes.id],
                quote = it[Quotes.quote],
                author = it[Quotes.author]
              )
            }
          .singleOrNull() ?: throw Exception("Quote not found")
      }
    }
    suspend fun update(id: Int, quote: Quote) = withContext(Dispatchers.IO) {
      transaction {
        Quotes.update({ Quotes.id eq id }) {
          it[this.quote] = quote.quote
          it[this.author] = quote.author
        }
      }
    }
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        //transaction {
        //    Quotes.deleteWhere { Quotes.id eq id }
        //}
    }
    suspend fun list(): List<Quote> = withContext(Dispatchers.IO) {
        transaction {
            Quotes.selectAll().map {
                Quote(
                    id = it[Quotes.id],
                    quote = it[Quotes.quote],
                    author = it[Quotes.author]
                )
            }
        }
    }
}
