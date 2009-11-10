package com.cedarsoft.file;

import com.cedarsoft.file.FileName;
import com.cedarsoft.file.FileTypeRegistry;
import com.cedarsoft.TestUtils;
import com.cedarsoft.file.Directory;
import org.apache.commons.io.FileUtils;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class DirectoryTest {
  @Test
  public void testIt() {
    Directory directory = new Directory();

    directory.addFile( new FileName( "img_0001", ".", "jpg" ) );
    directory.addFile( new FileName( "img_0001", ".", "cr2" ) );
    directory.addFile( new FileName( "img_0002", ".", "jpg" ) );
    directory.addFile( new FileName( "img_0002", ".", "cr2" ) );
    directory.addFile( new FileName( "img_0003", ".", "jpg" ) );

    assertEquals( directory.getFiles().size(), 5 );
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

      Directory directory = Directory.read( dir, new FileTypeRegistry() );

      assertEquals( directory.getFiles().size(), 6 );
    } finally {
      FileUtils.deleteDirectory( dir );
    }
  }
}
