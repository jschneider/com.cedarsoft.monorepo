package com.cedarsoft.utils;

import com.cedarsoft.utils.io.FileCopyManager;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p/>
 * Date: 25.08.2006<br>
 * Time: 17:07:01<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class FileCopyTest {
  private File tmp;
  private File myFile;


  @BeforeMethod
  protected void setUp() throws Exception {
    tmp = new File( "/tmp/fileCopyTestTemp" );
    tmp.mkdir();
    myFile = new File( tmp, "test.txt" );

    FileOutputStream out = new FileOutputStream( myFile );
    out.write( 60 );
    out.close();
  }

  @Test
  public void testCopySimple() throws IOException {
    File dst = new File( "/tmp/fileCopyTestTempTarget" );
    FileCopyManager.copy( tmp, dst );
    assertTrue( dst.exists() );
    assertEquals( 1, dst.list().length );
    assertEquals( "test.txt", dst.list()[0] );
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    myFile.delete();
    tmp.delete();


  }
}
