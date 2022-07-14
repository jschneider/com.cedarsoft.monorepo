package it.neckar.uuid

import assertk.*
import assertk.assertions.*
import com.benasher44.uuid.Uuid
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
}


