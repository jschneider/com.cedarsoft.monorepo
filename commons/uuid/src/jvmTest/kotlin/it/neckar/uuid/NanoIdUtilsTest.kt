package it.neckar.uuid

import assertk.*
import assertk.assertions.*
import com.benasher44.uuid.bytes
import com.benasher44.uuid.uuid4
import com.cedarsoft.common.kotlin.lang.random
import com.cedarsoft.test.utils.RandomWithSeed
import org.junit.jupiter.api.Test

class NanoIdUtilsTest {
  @RandomWithSeed(75)
  @Test
  fun testIt() {
    val id = NanoId.randomNanoId(random = random)
    assertThat(id).isEqualTo("xWJBvGD_7GyzKpRglSLMx")

    assertThat(id).hasLength(21)
    assertThat(NanoId.DEFAULT_ALPHABET).hasSize(64) //6 bits of information
    assertThat(21 * 6).isEqualTo(126)
  }

  @Test
  fun testCompareToUuid() {
    assertThat(uuid4().bytes).hasSize(16)
    assertThat(16 * 8).isEqualTo(128) //UUID has 128 Bits - but 6 bits are hard coded
  }
}
