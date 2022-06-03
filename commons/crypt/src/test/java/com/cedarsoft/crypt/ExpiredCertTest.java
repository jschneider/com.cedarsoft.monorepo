package com.cedarsoft.crypt;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

/**
 */
public class ExpiredCertTest {

  private X509Support x509Support;

  @BeforeEach
  public void setUp() throws Exception {
    x509Support = new X509Support(getClass().getResource("/test_expired.crt"), getClass().getResource("/test_expired.der"));
  }

  @Test
  public void testDate() throws Exception {
    byte[] plainText = "Hello World".getBytes(StandardCharsets.UTF_8);
    Signature signature = x509Support.sign(plainText);

    Assertions.assertThat(x509Support.verifySignature(plainText, signature)).isTrue();
  }
}
