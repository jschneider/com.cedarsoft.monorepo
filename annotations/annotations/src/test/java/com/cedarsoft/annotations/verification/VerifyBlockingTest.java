package com.cedarsoft.annotations.verification;

import javax.annotation.Nullable;

import org.junit.*;

import com.cedarsoft.test.utils.CatchAllExceptionsRule;
import com.cedarsoft.test.utils.ThreadRule;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VerifyBlockingTest {
  @Rule
  public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();
  @Rule
  public ThreadRule threadRule = new ThreadRule();

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
  public void testItShouldFail() throws Exception {
    NotStuckVerifier notStuckVerifier = NotStuckVerifier.start();
    callBlockingMethod();
    notStuckVerifier.finished();
  }

  @Test
  public void testItShouldWork() throws Exception {
    NotStuckVerifier notStuckVerifier = NotStuckVerifier.start();
    callNonBlockingMethod();
    notStuckVerifier.finished();
  }

  private void callNonBlockingMethod() {
  }

  private void callBlockingMethod() throws InterruptedException {
    Thread.sleep(1000L);
  }

}
