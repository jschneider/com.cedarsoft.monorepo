package com.cedarsoft.test.ktor

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class KtorClientServerTest {
  @Test
  internal fun testIt() {
    val port = 12345

    val server = embeddedServer(Netty, port) {
      install(ContentNegotiation) {
        jackson {
          enable(SerializationFeature.INDENT_OUTPUT)
        }
      }

      routing {
        post("/") {
          val message = call.receive<MyEnvelope>()
          println("SERVER: Message from the client: $message")
          call.respond(MyEnvelope(message = "response"))
        }
      }
    }.start(false)

    println("Server started")


    runBlocking {
      val client = HttpClient(Apache) {

        install(io.ktor.client.features.json.JsonFeature) {
          serializer = JacksonSerializer()
        }
      }

      val message = client.post<MyEnvelope> {
        url(URL("http://127.0.0.1:$port"))
        contentType(ContentType.Application.Json)
        body = MyEnvelope(message = "world")
      }

      println("CLIENT: Message from the server: $message")

      client.close()
      server.stop(1L, 1L, TimeUnit.SECONDS)
    }
  }
}


data class MyEnvelope(val message: String)


