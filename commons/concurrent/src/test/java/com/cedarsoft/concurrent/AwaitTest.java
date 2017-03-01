package com.cedarsoft.concurrent;

import com.cedarsoft.test.utils.ThreadRule;
import org.junit.*;

import javax.annotation.Nonnull;

import static org.awaitility.Awaitility.await;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AwaitTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();

  @Test
  public void checkThreadsLeft() throws Exception {
    await("asdf").until(() -> true);
  }
}
