package com.cedarsoft.commons

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 * Ensures the file has no \r - this ensures the repo has been checked out correctly
 */
class CrlfTest {
  @Test
  @Throws(Exception::class)
  fun testCrlfCheckout() {
    val resourceAsStream = javaClass.getResourceAsStream("/test-file.txt")

    assertThat(resourceAsStream).isNotNull()

    val content = resourceAsStream.readBytes()
    val contentAsString = content.toString(Charsets.UTF_8)

    assertThat(contentAsString).contains("\n")
    assertThat(contentAsString.contains("\r")).isFalse()
  }
}
