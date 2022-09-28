package it.neckar.react.common.router


/**
 * Represents a router variable that is encoded in the URL
 */
value class RouterVar(val value: String) {
  operator fun plus(relativePath: String): String {
    require(relativePath.startsWith("/").not()) {
      "Relative path must not start with / but was <$relativePath>"
    }
    return "${toPathString()}/$relativePath"
  }

  /**
   * Formats the value to be used as path string for routing (by adding ":")
   */
  fun toPathString(): String = ":$value"
}
