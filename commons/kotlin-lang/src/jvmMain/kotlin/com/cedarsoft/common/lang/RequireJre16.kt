package com.cedarsoft.common.lang

/**
 * Throws an exception, if the current JVM is nut running with Java 16
 */
fun requireJre16() {
  val jreVersion = System.getProperty("java.version")
  require(jreVersion.startsWith("16.")) {
    "Invalid JRE version: <$jreVersion>"
  }
}
