package it.neckar.react.common.router

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.js.get
import react.router.*

/**
 * Extracts the variable from params
 */
fun useFromParams(variableName: RouterVar): String {
  return requireNotNull(useParams()[variableName.value]) {
    "No parameter found for <$variableName>"
  }
}

/**
 * Returns a parameter value as int
 */
fun useFromParamsInt(variableName: RouterVar): Int {
  val valueAsString = useFromParams(variableName)
  return checkNotNull(valueAsString.toIntOrNull()) {
    "Could not parse value <$valueAsString> for variable $variableName to int"
  }
}

/**
 * Extracts a UUID from params
 * Throws an exception if the UUID could not be found
 */
fun useFromParamsUuid(variableName: RouterVar): Uuid {
  return uuidFrom(useFromParams(variableName))
}
