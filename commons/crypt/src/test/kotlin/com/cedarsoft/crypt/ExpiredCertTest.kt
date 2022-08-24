package com.cedarsoft.crypt

import com.cedarsoft.common.resources.getResourceSafe
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

/**
 */
class ExpiredCertTest {
  private var x509Support: X509Support? = null

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    x509Support = X509Support(javaClass.getResourceSafe("/test_expired.crt"), javaClass.getResourceSafe("/test_expired.der"))
  }

  @Test
  @Throws(Exception::class)
  fun testDate() {
    val plainText = "Hello World".toByteArray(StandardCharsets.UTF_8)
    val signature = x509Support!!.sign(plainText)
    Assertions.assertThat(x509Support!!.verifySignature(plainText, signature)).isTrue
  }
}
