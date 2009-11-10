package com.cedarsoft.file;

import com.cedarsoft.TestUtils;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

/**
 *
 */
public class FileNamesTest {
  @Test
  public void testIt2() {
    FileNames fileNames = new FileNames();

    assertEquals( fileNames.getFileNames().size(), 0 );
    fileNames.add( new FileName( "asdf", "txt" ) );
    assertEquals( fileNames.getFileNames().size(), 1 );
    fileNames.add( new FileName( "asdf", "jpg" ) );
    assertEquals( fileNames.getFileNames().size(), 2 );

    try {
      fileNames.add( new FileName( "asdf", "jpg" ) );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
    assertEquals( fileNames.getFileNames().size(), 2 );
  }

  @Test
  public void testIt() {
    FileNames fileNames = new FileNames();

    fileNames.add( new FileName( "img_0001", ".", "jpg" ) );
    fileNames.add( new FileName( "img_0001", ".", "cr2" ) );
    fileNames.add( new FileName( "img_0002", ".", "jpg" ) );
    fileNames.add( new FileName( "img_0002", ".", "cr2" ) );
    fileNames.add( new FileName( "img_0003", ".", "jpg" ) );

    assertEquals( fileNames.getFileNames().size(), 5 );
  }

  @Test
  public void testParse() throws IOException {
    File dir = TestUtils.createEmptyTmpDir();

    try {
      FileUtils.touch( new File( dir, "1.jpg" ) );
      FileUtils.touch( new File( dir, "2.jpg" ) );
      FileUtils.touch( new File( dir, "3.jpg" ) );
      FileUtils.touch( new File( dir, "4.jpg" ) );
      FileUtils.touch( new File( dir, "5.jpg" ) );
      FileUtils.touch( new File( dir, "5.cr2" ) );

      FileNames fileNames = new FileNamesFactory( new FileTypeRegistry() ).create( dir );

      assertEquals( fileNames.getFileNames().size(), 6 );
    } finally {
      FileUtils.deleteDirectory( dir );
    }
  }
}
