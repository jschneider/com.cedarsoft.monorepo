package com.cedarsoft.unit;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MeterTest {
  @Test
  public void testToString() throws Exception {
    assertEquals( "m", Units.getSymbol( m.class ) );
  }
}
