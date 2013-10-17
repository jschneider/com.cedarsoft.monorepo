package com.cedarsoft.annotations.instrumentation.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import org.junit.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class BaseClassTest {
  @Test
  public void testName() throws Exception {
    assert "asdf" != null;

    try {
      new BaseClass(null);
      fail("Where is the Exception");
    } catch (Exception e) {
      assertThat(e).hasMessage("Parameter 1 must not be null");
    }
  }
}
