package com.cedarsoft;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JavaLetter {
  @Test
  public void testName() throws Exception {
    assertTrue( Character.isJavaIdentifierStart( 'a' ) );
    assertTrue( Character.isDefined( '²' ) );
    assertTrue( Character.isValidCodePoint( '²' ) );

    assertFalse( Character.isJavaIdentifierPart( '²' ) );
    assertFalse( Character.isJavaIdentifierStart( '²' ) );
  }
}
