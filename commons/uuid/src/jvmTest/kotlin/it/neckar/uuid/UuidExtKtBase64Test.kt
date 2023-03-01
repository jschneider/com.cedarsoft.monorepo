package it.neckar.uuid

import assertk.*
import assertk.assertions.*
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import it.neckar.open.test.utils.RandomWithSeed
import org.junit.jupiter.api.Test

class UuidExtKtBase64Test {

  @Test
  fun testBase64Url() {
    val id: Uuid = uuidFrom("f4dbf95d-aed2-4219-9255-8aad57fec3df")
    roundTripBase64Url(id, "9Nv5Xa7SQhmSVYqtV_7D3w")
  }

  @RandomWithSeed(42)
  @Test
  fun testBase64UrlWithRandomUUID() {
    val id: Uuid = randomUuid4()
    roundTripBase64Url(id, "Oe_MGme_IEDnqz_J-Ucx3A")
  }

  private fun roundTripBase64Url(toEncode: Uuid, expectedResult: String) {
    // Encoding to Base64 URL safe
    val encoded = toEncode.encodeForUrl()
    assertThat(encoded).isEqualTo(expectedResult)
    assertThat(encoded).hasLength(22)
    // Decoding back to Uuid
    val decoded = decodeUuidFromUrl(encoded)
    assertThat(toEncode).isEqualTo(decoded)
  }
}
