package com.cedarsoft.common.lang

/**
 * Throws an exception, if the current JVM is nut running with Java 17
 */
fun requireJre17() {
  val jreVersion = System.getProperty("java.version")
  require(jreVersion.startsWith("17.")) {
    "Invalid JRE version: <$jreVersion>"
  }
}
