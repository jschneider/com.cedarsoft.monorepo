package com.cedarsoft.file;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class SameBaseNameTest {
  @Test
  public void testIt() {
    SameBaseName sameBaseName = new SameBaseName( new BaseName( "asdf" ) );

    assertEquals( sameBaseName.getFileNames().size(), 0 );
    sameBaseName.add( new FileName( "asdf", "txt" ) );
    assertEquals( sameBaseName.getFileNames().size(), 1 );
    sameBaseName.add( new FileName( "asdf", "jpg" ) );
    assertEquals( sameBaseName.getFileNames().size(), 2 );

    try {
      sameBaseName.add( new FileName( "asdf", "jpg" ) );
    } catch ( Exception ignore ) {
    }
    assertEquals( sameBaseName.getFileNames().size(), 2 );
  }

}
