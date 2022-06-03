package com.cedarsoft.commons.kotlin.serializers

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.time.Duration

/**
 */
internal class KotlinDurationSerializationTest : AbstractSerializationTest() {
  @Test
  fun testDuration() {
    assertThat(Duration.seconds(1)).isEqualTo(Duration.milliseconds(1000))
  }

  @Test
  internal fun testJson() {
    verifyJsonRoundTrip(Duration.days(7), KotlinDurationSerializer)
    verifyJsonRoundTrip(Duration.milliseconds(10), KotlinDurationSerializer)
    verifyJsonRoundTrip(Duration.hours(4), KotlinDurationSerializer)
    verifyJsonRoundTrip(Duration.microseconds(4.3), KotlinDurationSerializer)
    verifyJsonRoundTrip(Duration.seconds(122), KotlinDurationSerializer)
  }
}
