package com.cedarsoft.version

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class VersionInformationTest {
  @Test
  fun testIt() {
    assertThat(VersionInformation.version).startsWith("8.")
    assertThat(VersionInformation.gitCommit).isNotNull()
    assertThat(VersionInformation.buildDateDay).isNotNull()
    assertThat(VersionInformation.versionAsStringVerbose).contains(VersionInformation.version)
  }
}
