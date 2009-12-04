package com.cedarsoft;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class VersionRangeTest {
  @Test
  public void testIt() {
    VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) );

    assertTrue( range.contains( new Version( 1, 0, 0 ) ) );
    assertTrue( range.contains( new Version( 1, 1, 0 ) ) );
    assertTrue( range.contains( new Version( 1, 1, 89 ) ) );
    assertTrue( range.contains( new Version( 1, 1, 90 ) ) );

    assertFalse( range.contains( new Version( 1, 1, 91 ) ) );
    assertFalse( range.contains( new Version( 1, 2, 0 ) ) );
    assertFalse( range.contains( new Version( 0, 99, 99 ) ) );
  }

  @Test
  public void testEquals() {
    assertEquals( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ), new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ) );
    assertEquals( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ), new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ) );

    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ).equals( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 91 ) ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ).equals( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 1 ), new Version( 2, 1, 90 ) ).equals( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ) ) );
  }

  @Test
  public void testToString() {
    assertEquals( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ).toString(), "1.0.0-1.1.90" );
  }
}
