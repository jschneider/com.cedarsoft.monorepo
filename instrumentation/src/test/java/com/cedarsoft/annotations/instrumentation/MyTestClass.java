package com.cedarsoft.annotations.instrumentation;

import org.junit.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MyTestClass {
  public void atest() {
    String x = "before assert";
    System.out.println(x);
    assert x.length() > 5 : "hey";
    System.out.println("after assert");

    boolean desiredAssertionStatus = getClass().desiredAssertionStatus();
  }

  @Test
  public void testIt() throws Exception {
    atest();
  }
}
