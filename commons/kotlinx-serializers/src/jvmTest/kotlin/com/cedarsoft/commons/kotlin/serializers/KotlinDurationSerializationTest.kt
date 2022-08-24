package com.cedarsoft.commons.kotlin.serializers

import assertk.*
import assertk.assertions.*
import com.cedarsoft.commons.serialization.roundTrip
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 */
class KotlinDurationSerializationTest : AbstractSerializationTest() {
  @Test
  fun testDuration() {
    assertThat(1.seconds).isEqualTo(1000.milliseconds)
  }

  @Test
  fun testOldSerializer() {
    roundTrip(7.days, KotlinDurationSerializer) {
      "604800000000000".trimIndent()
    }

    roundTrip(10.milliseconds, KotlinDurationSerializer) {
      "10000000"
    }
    roundTrip(4.hours, KotlinDurationSerializer) {
      "14400000000000"
    }
    roundTrip(4.3.microseconds, KotlinDurationSerializer) {
      "4300"
    }
    roundTrip(122.seconds, KotlinDurationSerializer) {
      "122000000000"
    }
  }

  @Test
  fun testNewSerializer() {
    roundTrip(7.days) {
      """"PT168H"""".trimIndent()
    }

    roundTrip(10.milliseconds) {
      """
        "PT0.010S"
      """.trimIndent()
    }
    roundTrip(4.hours) {
      """
        "PT4H"
        """.trimMargin()
    }
    roundTrip(4.3.microseconds) {
      """
        "PT0.000004300S"
        """.trimMargin()
    }
    roundTrip(122.seconds) {
      """
        "PT2M2S"
      """.trimIndent()
    }
  }
}
