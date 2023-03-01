package it.neckar.open.time

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

/**
 *
 */
class DurationExtKtTest {
  @Test
  fun testFormat() {
    assertThat(7.minutes.formatHourAndMinutesShort()).isEqualTo("0:07")
    assertThat(0.minutes.formatHourAndMinutesShort()).isEqualTo("0:00")
    assertThat(59.minutes.formatHourAndMinutesShort()).isEqualTo("0:59")
    assertThat(60.minutes.formatHourAndMinutesShort()).isEqualTo("1:00")
    assertThat(61.minutes.formatHourAndMinutesShort()).isEqualTo("1:01")
    assertThat(121.minutes.formatHourAndMinutesShort()).isEqualTo("2:01")
  }
}
