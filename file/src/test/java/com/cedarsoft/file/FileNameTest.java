package com.cedarsoft.file;

import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class FileNameTest {
  @Test
  public void testDel() {
    assertEquals( new FileName( "asdf", ".", "jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( new FileName( "asdf", ".", "jpg" ).getExtension(), "jpg" );
    assertEquals( new FileName( "asdf", ".", "jpg" ).getDelimiter(), "." );

    assertEquals( new FileName( "asdf", ".", "jpg" ).getName(), "asdf.jpg" );
    assertEquals( new FileName( "asdf", ".", "jpg" ).toString(), "asdf.jpg" );
  }

  @Test
  public void testCase() {
    assertEquals( new FileName( "asdf", ".", "jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( new FileName( "aSdf", ".", "JPG" ).getBaseName().getName(), "aSdf" );

    assertEquals( new FileName( "asdf", ".", "jpg" ).getExtension(), "jpg" );
    assertEquals( new FileName( "asdf", ".", "JPG" ).getExtension(), "JPG" );

    assertEquals( new FileName( "asdf", ".", "jpg" ).getDelimiter(), "." );
  }

  @Test
  public void testFileNameUtils() {
    assertEquals( FilenameUtils.getExtension( "asdf" ), "" );
    assertEquals( FilenameUtils.getExtension( "asdf." ), "" );

    assertEquals( FilenameUtils.getBaseName( "asdf." ), "asdf" );
  }
}
