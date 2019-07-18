package com.cedarsoft.tests.gradle.deploy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class ClassToDeployTest {
  @Test
  internal fun testIt() {
    val classToDeploy = ClassToDeploy("daName")
    assertEquals("daName", classToDeploy.name)
  }

  @Disabled
  @Test
  internal fun testFailing() {
    fail("failing")
  }
}
