package com.cedarsoft.properties;

import static org.testng.Assert.*;
import org.testng.annotations.*;

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
