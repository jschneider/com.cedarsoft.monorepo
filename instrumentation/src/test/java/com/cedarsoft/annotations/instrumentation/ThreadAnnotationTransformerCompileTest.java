package com.cedarsoft.annotations.instrumentation;

import org.junit.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ThreadAnnotationTransformerCompileTest {
  @Test
  public void testIt() throws Exception {
    com.cedarsoft.annotations.verification.VerifyThread.verifyThread("a", "b");
  }
}
