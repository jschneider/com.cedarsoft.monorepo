package com.cedarsoft.annotations.instrumentation.test;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ConstructorNullTest {
  @Test
  public void testNull() throws Exception {
    try {
      new ConstructorNull( null );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
      assertThat( e ).hasMessage( "Parameter 1 must not be null" );
    }
  }

  @Test
  public void testNotNull() throws Exception {
    new ConstructorNull( "asdf" );
  }
}
