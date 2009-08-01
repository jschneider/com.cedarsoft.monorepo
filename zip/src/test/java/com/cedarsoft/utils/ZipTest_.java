package com.cedarsoft.utils;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

/**
 * Platform dependen test!
 */
@Deprecated
public class ZipTest_ {
  @Test
  public void testIt() throws IOException {
    File file = new File( "/tmp/asdf" );
    assertFalse( file.isDirectory() );
    assertEquals( "asdf", ZipCreator.getRelativePath( "/tmp", file ) );
  }

  @Test
  public void testDirectory() throws IOException {
    File file = new File( "/tmp/asdf_" );
    file.mkdirs();
    assertTrue( file.isDirectory() );
    assertEquals( "asdf_/", ZipCreator.getRelativePath( "/tmp", file ) );
  }
}
