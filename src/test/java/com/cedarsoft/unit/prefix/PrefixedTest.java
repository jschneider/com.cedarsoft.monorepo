package com.cedarsoft.unit.prefix;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class PrefixedTest {
  @Test
  public void testIt() {
    assertEquals( 0.1, Prefixed.getFactor( centi.class ), 0 );
    assertEquals( 1000, Prefixed.getFactor( kilo.class ), 0 );
  }
}
