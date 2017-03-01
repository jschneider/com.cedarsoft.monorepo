package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nullable;

import org.junit.*;

import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.verification.NotStuckVerifier;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonBlockingTest {
  @Nullable
  private NotStuckVerifier.ThreadStuckEvaluator oldEvaluator;

  @Before
  public void setUp() throws Exception {
    oldEvaluator = NotStuckVerifier.getEvaluator();
    NotStuckVerifier.setEvaluator(new NotStuckVerifier.ExceptionThrowingEvaluator(10));
  }

  @After
  public void tearDown() throws Exception {
    NotStuckVerifier.setEvaluator(oldEvaluator);
  }

  @Test(expected = IllegalThreadStateException.class)
  @NonBlocking
  public void testFailWhenInstrumented() throws Exception {
    Thread.sleep(1000);
  }
}
