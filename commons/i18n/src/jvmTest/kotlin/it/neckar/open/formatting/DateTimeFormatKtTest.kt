package it.neckar.open.formatting

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.WithLocale
import org.junit.jupiter.api.Test

class DateTimeFormatKtTest {
  @WithLocale("en_US")
  @Test
  fun testFormatDuration() {
    assertThat(1001.0.formatAsDuration()).isEqualTo("1s 1.0ms")
    assertThat(123123123123123.0.formatAsDuration()).isEqualTo("1,425,036 days 3h 32min 3s 123.0ms")
  }
}
