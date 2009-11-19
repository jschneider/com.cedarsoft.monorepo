package com.cedarsoft;

import org.testng.annotations.*;

import static org.testng.Assert.*;

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

  @Test
  public void testCompareGreater() {
    assertTrue( new Version( 1, 2, 3 ).sameOrGreaterThan( new Version( 1, 2, 3 ) ) );
    assertTrue( new Version( 1, 2, 3 ).sameOrGreaterThan( new Version( 1, 2, 2 ) ) );
    assertTrue( new Version( 1, 2, 3 ).sameOrGreaterThan( new Version( 0, 2, 2 ) ) );

    assertFalse( new Version( 1, 2, 3 ).sameOrGreaterThan( new Version( 1, 2, 4 ) ) );
  }

  @Test
  public void testCompareSmaller() {
    assertTrue( new Version( 1, 2, 3 ).sameOrSmallerThan( new Version( 1, 2, 3 ) ) );
    assertTrue( new Version( 1, 2, 3 ).sameOrSmallerThan( new Version( 1, 2, 4 ) ) );
    assertTrue( new Version( 1, 2, 3 ).sameOrSmallerThan( new Version( 2, 2, 2 ) ) );

    assertFalse( new Version( 1, 2, 3 ).sameOrSmallerThan( new Version( 0, 2, 4 ) ) );
  }

}
