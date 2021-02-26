package com.cedarsoft.test.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@WithLocale("ja_JP")
public class LocaleExtensionTest {
  @Test
  void testWithout() {
    //Value from "global" config for complete test
    assertThat(Locale.getDefault()).isEqualTo(Locale.JAPAN);
  }

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
