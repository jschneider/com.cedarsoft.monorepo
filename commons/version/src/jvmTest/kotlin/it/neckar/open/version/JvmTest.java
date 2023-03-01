package it.neckar.open.version;

import static org.assertj.core.api.Assertions.*;

import java.lang.management.ManagementFactory;

import org.junit.jupiter.api.*;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * Verifies the toolchain
 */
public class JvmTest {
  @Test
  void testJdk() {
    assertThat(ManagementFactory.getRuntimeMXBean().getVmVendor()).isEqualTo("Oracle Corporation");
    assertThat(ManagementFactory.getRuntimeMXBean().getSpecVersion()).isEqualTo("1.8");

    new SimpleBooleanProperty();
  }
}
