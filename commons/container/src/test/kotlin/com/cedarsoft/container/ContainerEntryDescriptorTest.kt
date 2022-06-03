package com.cedarsoft.container

import assertk.*
import assertk.assertions.*
import com.cedarsoft.version.Version
import org.junit.jupiter.api.Test

/**
 */
internal class ContainerEntryDescriptorTest {
  @Test
  internal fun testPathParse() {
    val descriptor = ContainerEntryDescriptor("data/test/foo", Version.Companion.of(1, 2, 3))
    assertThat(descriptor.path).isEqualTo("data/test/foo/1.2.3")
    assertThat(ContainerEntryDescriptor.fromPath(descriptor.path)).isEqualTo(descriptor)
  }
}
