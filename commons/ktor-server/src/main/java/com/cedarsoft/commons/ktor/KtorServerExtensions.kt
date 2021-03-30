package com.cedarsoft.commons.ktor

import io.ktor.http.*

/**
 * Extension methods for the Ktor server
 */
fun Parameters.getParamSafe(paramName: String): String {
  return this[paramName] ?: throw IllegalArgumentException("Missing required query parameter <$paramName>")
}
