package com.cedarsoft.test.utils

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.kotlin.lang.random
import org.junit.jupiter.api.Test
import kotlin.random.Random

class WithSeededRandomProviderExtensionTest {
  @RandomWithSeed(55)
  @Test
  fun testIt() {
    assertThat(random).isNotSameAs(Random.Default)

    assertThat(random.nextInt()).isEqualTo(-2023281093)
    assertThat(Random(55).nextInt()).isEqualTo(-2023281093)
  }
}
