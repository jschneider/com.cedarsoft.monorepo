package com.cedarsoft.test.utils;

import org.junit.jupiter.api.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class DisableWhenHeadlessConditionTest {
  @DisableIfHeadless
  @Test
  void testItIfHeadless() throws Exception {
    System.out.println("Running");
  }
}
