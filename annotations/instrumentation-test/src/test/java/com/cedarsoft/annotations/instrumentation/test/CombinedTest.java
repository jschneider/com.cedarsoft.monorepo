package com.cedarsoft.annotations.instrumentation.test;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CombinedTest {
  @Test
  public void testName() throws Exception {
    try {
      new Combined().doIt();
      fail("Where is the Exception");
    } catch (Exception e) {
      assertThat(e).hasMessage("Return value must not be null for method annotated with @Nonnull");
    }
  }
}
