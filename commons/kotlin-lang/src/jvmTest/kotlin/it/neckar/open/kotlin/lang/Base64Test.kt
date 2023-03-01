package it.neckar.open.kotlin.lang

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

/**
 */
class Base64Test {
  @Test
  fun testEncoding() {
    assertThat("asdf1234".toBase64()).isEqualTo("YXNkZjEyMzQ=")
    assertThat("asdf1234".encodeToByteArray().toBase64()).isEqualTo("YXNkZjEyMzQ=")
  }

  @Test
  fun testDeltaUrl() {
    val content = byteArrayOf(252.toByte())
    assertThat(content.toBase64()).isNotEqualTo(content.toBase64Url())
  }

  @Test
  fun testBase64() {
    roundTripBase64("helloWorld".encodeToByteArray(), "aGVsbG9Xb3JsZA==")
    roundTripBase64("~g1\$".encodeToByteArray(), "fmcxJA==")
    roundTripBase64("$\"+~".encodeToByteArray(), "JCIrfg==")
    roundTripBase64(byteArrayOf(244.toByte(), 11.toByte(), Byte.MIN_VALUE, Byte.MAX_VALUE), "9AuAfw==")
    roundTripBase64(byteArrayOf(252.toByte()), "/A==")
  }

  private fun roundTripBase64(toEncode: ByteArray, expectedBase64: String) {
    val encoded = toEncode.toBase64()
    assertThat(encoded).isEqualTo(expectedBase64)
    assertThat(encoded.fromBase64String()).isEqualTo(toEncode.decodeToString())
  }

  @Test
  fun testBase64Url() {
    roundTripBase64Url("helloWorld".encodeToByteArray(), "aGVsbG9Xb3JsZA")
    roundTripBase64Url("$\"+~".encodeToByteArray(), "JCIrfg")
    roundTripBase64Url(byteArrayOf(244.toByte(), 11.toByte(), Byte.MIN_VALUE, Byte.MAX_VALUE), "9AuAfw")
    roundTripBase64Url(byteArrayOf(252.toByte()), "_A")
  }

  private fun roundTripBase64Url(toEncode: ByteArray, expectedBase64: String) {
    val encoded = toEncode.toBase64UrlString()
    assertThat(encoded).isEqualTo(expectedBase64)
    assertThat(encoded.fromBase64UrlString()).isEqualTo(toEncode.decodeToString())
  }

}
