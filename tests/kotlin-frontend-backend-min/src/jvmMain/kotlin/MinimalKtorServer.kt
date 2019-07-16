package com.cedarsoft.open.kfbmin.server

import com.cedarsoft.commons.ktor.JsonSerializationConverter
import com.cedarsoft.commons.ktor.kotlinxSerialization
import com.cedarsoft.open.kfbmin.HelloWorldData
import com.cedarsoft.open.kfbmin.serializersModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@UnstableDefault
@ExperimentalCoroutinesApi
fun main(args: Array<String>) {
  var port = 8099

  if (args.isNotEmpty()) {
    try {
      port = Integer.parseInt(args[0])
    } catch (e: Exception) {
      System.err.println("Could not parse port <" + args[0] + "> due to " + e.message)
    }
  }

  embeddedServer(Netty, port, module = {
    launchServer()
  }).start(wait = true)
}

class MinimalKtorServer() {
  @UnstableDefault
  val json: Json = Json(configuration = JsonConfiguration.Default, context = serializersModule)

  companion object {
    val logger: Logger by LoggerDelegate()
    val requestLogger: Logger = LoggerFactory.getLogger("Requests")
  }
}

@ExperimentalCoroutinesApi
@UnstableDefault
internal fun Application.launchServer(server: MinimalKtorServer = MinimalKtorServer()) {
  install(DefaultHeaders)
  install(CallLogging) {
    level = Level.INFO
    logger = MinimalKtorServer.requestLogger
  }

  install(ContentNegotiation) {
    kotlinxSerialization(JsonSerializationConverter(server.json)) {
      register(HelloWorldData.serializer())
    }
  }

  routing(Routing(server).modelRouting)
}

var data = HelloWorldData("Hello World")

class Routing(server: MinimalKtorServer) {
  @UnstableDefault
  @ExperimentalCoroutinesApi
  val modelRouting: Routing.() -> Unit = {
    //Provide the index.html
    static("") {
      defaultResource("index.html")
    }

    //Provide the java script file loaded from the index.html
    static("js") {
      resources("js")
    }

    route("hello-world") {
      get {
        MinimalKtorServer.logger.info("GET hello world called - returning $data")
        context.respond(HttpStatusCode.OK, data)
      }

      put {
        val jsonString = context.request.call.receiveText()
        MinimalKtorServer.logger.info("PUT hello world called - with $jsonString")
        val receivedData = server.json.parse(HelloWorldData.serializer(), jsonString)
        com.cedarsoft.open.kfbmin.server.data = receivedData
        call.respond(HttpStatusCode.OK)
      }
    }
  }
}


