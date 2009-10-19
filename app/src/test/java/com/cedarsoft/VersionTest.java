package com.cedarsoft;

import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class VersionTest {
  @Test
  public void testEquals() {
    assertEquals( new Version( 1, 2, 3 ), new Version( 1, 2, 3 ) );
    assertEquals( new Version( 1, 2, 3, "asdf" ), new Version( 1, 2, 3, "asdf" ) );
  }

  @Test
  public void testToString() {
    assertEquals( new Version( 1, 2, 3 ).toString(), "1.2.3" );
    assertEquals( new Version( 1, 2, 3, "asdf" ).toString(), "1.2.3-asdf" );
  }

  @Test
  public void testParse() {
    assertEquals( new Version( 1, 2, 3 ), Version.parse( "1.2.3" ) );
    assertEquals( new Version( 1, 2, 3, "build76" ), Version.parse( "1.2.3-build76" ) );
    assertEquals( new Version( 1, 2, 3, "build76" ), Version.parse( new Version( 1, 2, 3, "build76" ).toString() ) );
  }

}
