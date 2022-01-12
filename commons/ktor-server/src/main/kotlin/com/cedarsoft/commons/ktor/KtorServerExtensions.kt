package com.cedarsoft.commons.ktor

import com.cedarsoft.common.collections.fastForEach
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import it.neckar.common.featureflags.FeatureFlag
import it.neckar.common.featureflags.FeatureFlags
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * Extension methods for the Ktor server
 */
fun Parameters.getParamSafe(paramName: String): String {
  return this[paramName] ?: throw IllegalArgumentException("Missing required query parameter <$paramName>")
}

/**
 * Collects all routes
 */
fun Application.collectRoutes(): List<Route> {
  fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
  }

  val routing = plugin(Routing)
  return allRoutes(routing)
}

/**
 * Dumps all routes as string
 */
fun Application.dumpRoutes(): String {
  return buildString {
    collectRoutes().fastForEach {
      appendLine(it.toString())
    }
  }
}

fun Application.installCallLogging(
  level: Level = Level.DEBUG,
  logger: Logger = LoggerFactory.getLogger("Requests")
) {
  install(CallLogging) {
    this.level = level
    this.logger = logger
  }
}

/**
 * Returns true if this request is a CORS preflight check
 */
val ApplicationRequest.isCorsRequest: Boolean
  get() {
    return httpMethod == HttpMethod.Options && header("Access-Control-Request-Method") != null
  }

/**
 * Extracts the feature flags
 */
fun PipelineContext<Unit, ApplicationCall>.extractFeatureFlags(): FeatureFlags {
  return FeatureFlags.decodeFromString(call.request.header(FeatureFlags.HeaderKey))
}

/**
 * This method delays the UI if the corresponding feature flag is enabled
 */
suspend fun FeatureFlags.delayIfSlowServerEnabled() {
  if (contains(FeatureFlag.slowServer)) {
    println("Delaying response because of FeatureFlag [${FeatureFlag.slowServer}]")
    delay(1000)
  }
}
