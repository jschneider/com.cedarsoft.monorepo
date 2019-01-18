package com.cedarsoft.test.ktor

import com.codahale.metrics.JmxReporter
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.metrics.Metrics
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Runs the ktor server
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
fun main(args: Array<String>) {
  val server = embeddedServer(Netty, 8080) {
    install(Metrics) {
      JmxReporter.forRegistry(registry)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build()
        .start()

      //      Slf4jReporter.forRegistry(registry)
      //        .outputTo(log)
      //        .convertRatesTo(TimeUnit.SECONDS)
      //        .convertDurationsTo(TimeUnit.MILLISECONDS)
      //        .build()
      //        .start(10, TimeUnit.SECONDS)
    }


    install(ContentNegotiation) {
      jackson {
        enable(SerializationFeature.INDENT_OUTPUT)
      }
    }

    install(StatusPages) {
      exception<MyException> {
        call.respond(HttpStatusCode.BadRequest, mapOf("OK" to false, "error" to it.message))
      }
    }

    val startTime = System.currentTimeMillis()

    val counter = AtomicInteger().also {
      val threadPool = Executors.newScheduledThreadPool(1)
      threadPool.scheduleWithFixedDelay(
        {
          it.incrementAndGet()
        }, 1000, 10, TimeUnit.MILLISECONDS
      )
    }


    routing {
      get("/") {
        call.respondText("Hello World", ContentType.Text.Plain)
      }

      get("/demo") {
        call.respondText("""{"message":"Hello World"}""", ContentType.Application.Json)
      }

      get("/memory") {
        call.respondText(
          "Total memory: ${Runtime.getRuntime().totalMemory().toMB()} MB\n" +
            "Free memory: ${Runtime.getRuntime().freeMemory().toMB()} MB\n" +
            "Max memory: ${Runtime.getRuntime().maxMemory().toMB()} MB\n", ContentType.Text.Plain
        )
      }

      get("/counter") {
        val now = System.currentTimeMillis()
        val delta = now - startTime

        val expectedCounter = Math.max(1, (delta - 1000) / 10)

        call.respondText("The current value is ${counter.get()}. After $delta ms\nExpected counter: $expectedCounter", ContentType.Text.Plain)
      }

      get("/person") {
        call.respond(Person("Markus Mustermann", 40))
      }

      get("/persons") {
        call.respond(listOf(Person("Markus Mustermann", 40), Person("Markus Mustermann2", 42)))
      }

      get("/state") {
        call.respond(mapOf("Ok" to true))
      }


      route("snippets") {
        post {
          val snippet = call.receive<PostSnippet>()
          println("snippet posted = ${snippet}")

          if (snippet.text.length < 10) {
            throw MyException("Snippet too short")
          }

          call.respond(mapOf("OK" to true))
        }

        get {
          call.respond(mapOf("Snippets" to listOf("s1", "s2", "s3")))
        }
      }
    }
  }

  println("Starting up KTOR server")
  server.start(true)
  println("Finished")
}

private fun Long.toMB(): Long {
  return this / 1024 / 1024
}


data class PostSnippet(val text: String)

data class Person(val name: String, val age: Int)

class MyException(message: String) : RuntimeException(message)
