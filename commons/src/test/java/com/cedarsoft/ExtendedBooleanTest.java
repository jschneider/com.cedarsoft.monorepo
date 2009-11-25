package com.cedarsoft;

import org.testng.annotations.*;

import java.util.Locale;

import static org.testng.Assert.*;

/**
 *
 */
public class ExtendedBooleanTest {
  @Test
  public void testTRanslation() {
    assertEquals( "Ja", ExtendedBoolean.TRUE.getDescription( Locale.GERMANY ) );
    assertEquals( "Yes", ExtendedBoolean.TRUE.getDescription( Locale.US ) );
    assertEquals( "Yes", ExtendedBoolean.TRUE.getDescription( Locale.FRANCE ) );

    assertEquals( "Nein", ExtendedBoolean.FALSE.getDescription( Locale.GERMANY ) );
    assertEquals( "No", ExtendedBoolean.FALSE.getDescription( Locale.US ) );
    assertEquals( "No", ExtendedBoolean.FALSE.getDescription( Locale.FRANCE ) );

    assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription( Locale.GERMANY ) );
    assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription( Locale.US ) );
    assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription( Locale.FRANCE ) );
  }
}
