package com.cedarsoft.properties;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class PropertiesPathTest {
  @Test
  public void testPresentation() {
    assertEquals( "a.b", new PropertiesPath( "a", "b" ).getPresentation() );
    assertEquals( "baseValues.profitParticipationType", new PropertiesPath( "baseValues", "profitParticipationType" ).getPresentation() );
  }

}
