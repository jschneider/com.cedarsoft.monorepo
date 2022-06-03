package com.cedarsoft.test.utils;

import org.junit.jupiter.api.*;

/**
 */
class DisableWhenHeadlessConditionTest {
  @DisableIfHeadless
  @Test
  void testItIfHeadless() throws Exception {
    System.out.println("Running");
  }
}
