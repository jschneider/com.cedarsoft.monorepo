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
  @Test
  fun additional() {
    val flags = FeatureFlags(FeatureFlag.slowUi, FeatureFlag("another"))
    val newFlags = flags.withAdditional(FeatureFlag.testing)

    assertThat(newFlags.encodeToString()).isEqualTo("slowUI,another,testing")
    assertThat(newFlags.contains(FeatureFlag.slowUi)).isTrue()
    assertThat(newFlags.contains(FeatureFlag("foo"))).isFalse()

    val duplicateFlags = newFlags.withAdditional(FeatureFlag.testing)
    assertThat(newFlags.encodeToString()).isEqualTo(duplicateFlags.encodeToString())
  }

  @Test
  fun remove() {
    val flags = FeatureFlags(FeatureFlag.slowUi, FeatureFlag.testing)

    assertThat(flags.encodeToString()).isEqualTo("slowUI,testing")
    assertThat(flags.withRemovedFlag(FeatureFlag.testing).encodeToString()).isEqualTo("slowUI")
  }

}


