package com.cedarsoft.tests.pwa.server

import com.cedarsoft.tests.pwa.server.GeolocationServer.Companion.logger
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

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
    launchPwa(GeolocationServer())
  }).start(wait = true)
}


@ExperimentalCoroutinesApi
class GeolocationServer() {
  companion object {
    val logger: Logger by LoggerDelegate()
    val requestLogger: Logger = LoggerFactory.getLogger("GeolocationServer-Requests")
  }
}


@ExperimentalCoroutinesApi
internal fun Application.launchPwa(server: GeolocationServer = GeolocationServer()) {
  install(StatusPages) {
    exception<IllegalArgumentException> { cause ->
      logger.warn("IAE", cause)

      val message: String = cause.message ?: ""
      call.respond(HttpStatusCode.BadRequest, message)
    }
    exception<Throwable> { cause ->
      logger.warn("Internal Server Error", cause)

      val message: String = cause.message ?: ""
      call.respond(HttpStatusCode.InternalServerError, message)
    }
  }
  install(DefaultHeaders)
  install(CallLogging) {
    level = Level.INFO
    logger = GeolocationServer.requestLogger
  }

  routing(GeolocationRouting().modelRouting)
}
