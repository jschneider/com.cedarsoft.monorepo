package com.cedarsoft.annotations.instrumentation.test2;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ObjectWithNullTest {
  @Test
  public void testConstructor() throws Exception {
    new ObjectWithNull( "asdf" );
    try {
      new ObjectWithNull( null );
      fail( "Where is the Exception" );
    } catch ( NullPointerException e ) {
      assertThat( e ).hasMessage( "Parameter 1 must not be null" );
    }
  }

  @Test
  public void testMethod() throws Exception {
    ObjectWithNull asdf = new ObjectWithNull( "asdf" );
    try {
      asdf.getS();
      fail( "Where is the Exception" );
    } catch ( NullPointerException e ) {
      assertThat( e ).hasMessage( "Return value must not be null for method annotated with @Nonnull" );
    }
  }
}
