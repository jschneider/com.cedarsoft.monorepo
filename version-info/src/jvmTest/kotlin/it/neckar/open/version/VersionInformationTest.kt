package it.neckar.open.version

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 */
class VersionInformationTest {
  @Test
  fun testIt() {
    assertThat(VersionInformation.version).startsWith("9.")
    assertThat(VersionInformation.gitCommit).isNotNull()
    assertThat(VersionInformation.buildDateDay).isNotNull()
    assertThat(VersionInformation.versionAsStringVerbose).contains(VersionInformation.version)
  }
}
