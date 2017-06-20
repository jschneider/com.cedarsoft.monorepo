package com.cedarsoft.test.utils;

import org.junit.jupiter.api.*;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LocaleExtensionTest {
  @WithLocale("de_DE")
  @Test
  void testDe() {
    assertThat(Locale.getDefault()).isEqualTo(Locale.GERMANY);
  }

  @WithLocale("en_US")
  @Test
  void testUs() {
    assertThat(Locale.getDefault()).isEqualTo(Locale.US);
  }
}
