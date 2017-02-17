package com.cedarsoft.concurrent;

import com.cedarsoft.test.utils.ThreadRule;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NamedThreadFactoryTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();

  @Test(timeout = 1000)
  public void simple() throws Exception {
    NamedThreadFactory factory = new NamedThreadFactory("daName");

    Thread thread = factory.newThread(() -> {
    });
    thread.start();

    assertThat(thread.getName()).isEqualTo("daName-1");
    thread.join();
  }
}