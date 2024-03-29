package it.neckar.open.kotlin.bytearray

import assertk.*
import assertk.assertions.*
import it.neckar.open.kotlin.lang.hex
import org.junit.jupiter.api.Test

/**
 */
class ByteArrayBuilderTest {
  @Test
  internal fun testIt() {
    val builder = ByteArrayBuilder()
    builder.append(byteArrayOf(1))
    builder.append(byteArrayOf(2, 3))
    builder.append(4)
    builder.append(byteArrayOf(5))

    assertThat(builder.toByteArray().hex).isEqualTo("0102030405")
  }

  @Test
  internal fun testMore() {
    val builder = ByteArrayBuilder()
    assertThat(builder.toByteArray().hex).isEqualTo("")

    builder.f32(12.0f, true)
    builder.f32LE(12.0f)

    assertThat(builder.toByteArray().hex).isEqualTo("0000404100004041")
  }

  @Test
  internal fun testMinimal() {
    val builder = ByteArrayBuilder()
    assertThat(builder.toByteArray().hex).isEqualTo("")

    builder.s8(0)
    assertThat(builder.toByteArray().hex).isEqualTo("00")

    builder.s8(1)
    assertThat(builder.toByteArray().hex).isEqualTo("0001")

    builder.s8(0xff)
    assertThat(0xff).isEqualTo(255)
    assertThat(builder.toByteArray().hex).isEqualTo("0001ff")
  }
}
