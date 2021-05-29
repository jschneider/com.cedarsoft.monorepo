package com.cedarsoft.common.time

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.time.Duration

/**
 *
 */
class DurationExtKtTest {
  @Test
  fun testFormat() {
    assertThat(Duration.Companion.minutes(7).formatHourMinutes()).isEqualTo("0:07")
    assertThat(Duration.Companion.minutes(0).formatHourMinutes()).isEqualTo("0:00")
    assertThat(Duration.Companion.minutes(59).formatHourMinutes()).isEqualTo("0:59")
    assertThat(Duration.Companion.minutes(60).formatHourMinutes()).isEqualTo("1:00")
    assertThat(Duration.Companion.minutes(61).formatHourMinutes()).isEqualTo("1:01")
    assertThat(Duration.Companion.minutes(121).formatHourMinutes()).isEqualTo("2:01")
  }
}
