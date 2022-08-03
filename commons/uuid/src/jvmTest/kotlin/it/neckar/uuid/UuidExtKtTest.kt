package it.neckar.uuid

import assertk.*
import assertk.assertions.*
import com.benasher44.uuid.Uuid
import com.cedarsoft.common.kotlin.lang.random
import com.cedarsoft.test.utils.RandomWithSeed
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 *
 */
class UuidExtKtTest {
  @Test
  fun test() {
    val first: Uuid = UUID.fromString("fef44139-de86-4ff2-a2a4-8c939377f203")
    val second = UUID.fromString("56ea54cc-3fb9-4705-a342-46f8fe235e3e")

    val newUuid = first.xor(second)

    assertThat(newUuid, "First Uuid: $first, New Uuid: $newUuid; should have been different!").isNotEqualTo(first)
    assertThat(newUuid, "Second Uuid: $second, New Uuid: $newUuid; should have been different!").isNotEqualTo(second)

    assertThat(newUuid.toString()).isEqualTo("a81e15f5-e13f-08f7-01e6-ca6b6d54ac3d")
  }

  @RandomWithSeed(99)
  @Test
  fun testRandom() {
    assertThat(random.nextInt(100)).isEqualTo(97)
    assertThat(random.nextInt(100)).isEqualTo(51)

    assertThat(pseudoRandomUuid4().toString()).isEqualTo("237f7e7d-db3c-84cb-7eed-ec9b569ac964")
    assertThat(pseudoRandomUuid4().toString()).isEqualTo("de4bd42b-70d1-381c-ad15-8476b776a4fe")
  }

  @RandomWithSeed(50)
  @Test
  fun testRandomSeed50() {
    assertThat(random.nextInt(100)).isEqualTo(54)
    assertThat(random.nextInt(100)).isEqualTo(86)

    assertThat(pseudoRandomUuid4().toString()).isEqualTo("25f0f998-45db-67c5-0cd0-7e1e297d09a5")
    assertThat(pseudoRandomUuid4().toString()).isEqualTo("96f66ffb-b5a9-377c-0bc4-b3cfb552db11")
  }

  @RandomWithSeed(50)
  @Test
  fun testRandomUuid() {
    assertThat(random.nextInt(100)).isEqualTo(54)
    assertThat(random.nextInt(100)).isEqualTo(86)

    //Works - because we are in a test Environment
    assertThat(randomUuid4().toString()).isEqualTo("25f0f998-45db-67c5-0cd0-7e1e297d09a5")
  }
}


