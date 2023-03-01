package it.neckar.open.kotlin.bytearray

import assertk.*
import assertk.assertions.*
import it.neckar.open.kotlin.lang.fromBase64
import it.neckar.open.kotlin.lang.toBase64
import org.junit.jupiter.api.Test

class Base64Test {
  val sampleString: String = "$\"+~"

  val sampleBytes: ByteArray = buildByteArray {
    append(17)
    append(sampleString.encodeToByteArray())
    append(5.toByte())
  }

  @Test
  fun testString1() {
    val encoded = sampleString.toBase64().also {
      assertThat(it).isEqualTo("JCIrfg==")
    }

    val decodedString = encoded.fromBase64().toString(Charsets.UTF_8)
    assertThat(decodedString).isEqualTo(sampleString)

  }

  @Test
  fun testByteArray() {
    val encoded = sampleBytes.toBase64().also {
      assertThat(it).isEqualTo("ESQiK34F")
    }

    val decoded = encoded.fromBase64()
    assertThat(decoded).isEqualTo(sampleBytes)
  }

  @Test
  fun testStringOwnImpl2() {
    val encoded = sampleBytes.toBase64().also {
      assertThat(it).isEqualTo("ESQiK34F")
    }

    val decoded = encoded.fromBase64()
    assertThat(decoded).isEqualTo(sampleBytes)
  }
}
