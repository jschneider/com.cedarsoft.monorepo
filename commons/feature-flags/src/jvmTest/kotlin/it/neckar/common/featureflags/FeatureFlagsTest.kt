package it.neckar.common.featureflags

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test


class FeatureFlagsTest {
  @Test
  fun testToString() {
    val flags = FeatureFlags(FeatureFlag.slowUi, FeatureFlag("another"))

    val string = flags.encodeToString()
    assertThat(string).isEqualTo("slowUI,another")
    assertThat(FeatureFlags.decodeFromString(string)).isEqualTo(flags)
  }

  @Test
  fun testEqualsNoDescription() {
    assertThat(FeatureFlag.slowUi).isEqualTo(FeatureFlag(FeatureFlag.slowUi.key))
  }

  @Test
  fun testEncodeUnknown() {
    FeatureFlags.decodeFromString(
      FeatureFlags(FeatureFlag("doesnotexist")).encodeToString()
    ).flags.let {
      assertThat(it).hasSize(1)
      assertThat(it.first().key).isEqualTo("doesnotexist")
    }
  }
}


