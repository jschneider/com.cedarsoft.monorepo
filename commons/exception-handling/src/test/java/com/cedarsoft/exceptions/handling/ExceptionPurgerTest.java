package com.cedarsoft.exceptions.handling;

import org.assertj.core.api.Assertions;
import org.junit.*;

import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ExceptionPurgerTest {
  @Test
  public void purgeTest() throws Exception {
    Throwable found = ExceptionPurger.purge(new RuntimeException(new IOException()));
    Assertions.assertThat(found).isInstanceOf(IOException.class);
  }
}
