package com.cedarsoft.version;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * Verifies the toolchain
 */
public class JvmTest {
  @Test
  void testJdk() {
    assertThat(java.lang.management.ManagementFactory.getRuntimeMXBean().getVmVendor()).isEqualTo("Oracle Corporation");
    assertThat(java.lang.management.ManagementFactory.getRuntimeMXBean().getSpecVersion()).isEqualTo("1.8");

    new SimpleBooleanProperty();
  }
}
