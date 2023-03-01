package it.neckar.open.unit

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class VerifyJvmDepTest {
  /**
   * This test ensures that the gradle configuration takes the correct JVM dependency
   */
  @Test
  fun testJvmVersionAtRuntime() {
    //Ensure the correct version at *RUNTIME*
    assertThat(System.getProperty("java.version")).startsWith("1.8")
  }

  @Test
  fun testJavaVersionCompile() {
    //Ensure Oracle Java 8 (with JavaFX) is available
    javafx.application.Platform::class.java.let {
      assertThat(it.name).isEqualTo("javafx.application.Platform")
    }
  }
}
