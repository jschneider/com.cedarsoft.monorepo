package com.cedarsoft.app;

import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class ApplicationTest {
  @Test
  public void testIt() {
    Application app = new Application( "Gimp", new Version( 3, 1, 6 ) );
    assertEquals( app.getName(), "Gimp" );
    assertEquals( app.getVersion(), new Version( 3, 1, 6 ) );
  }
}
