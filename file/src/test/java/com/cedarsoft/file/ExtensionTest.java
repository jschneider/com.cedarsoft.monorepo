package com.cedarsoft.file;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class ExtensionTest {
  @Test
  public void testDefault() {
    assertEquals( new Extension( ".", "txt" ).getCombined(), ".txt" );
    assertEquals( new Extension( "txt" ).getCombined(), ".txt" );
  }

  @Test
  public void testEquals() {
    assertEquals( new Extension( ".", "txt" ), new Extension( ".", "txt" ) );
    assertEquals( Extension.NONE, new Extension( "", "" ) );
  }

  @Test
  public void testEmpty() {
    assertEquals( new Extension( "", "" ).getCombined(), "" );
    assertEquals( new Extension( "txt" ).getCombined(), ".txt" );
  }

}
