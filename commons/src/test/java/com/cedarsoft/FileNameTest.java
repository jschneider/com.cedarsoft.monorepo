package com.cedarsoft;

import com.cedarsoft.FileName;
import org.apache.commons.io.FilenameUtils;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class FileNameTest {
  @Test
  public void testDel() {
    assertEquals( new FileName( "asdf", ".", "jpg" ).getBaseName(), "asdf" );
    assertEquals( new FileName( "asdf", ".", "jpg" ).getExtension(), "jpg" );
    assertEquals( new FileName( "asdf", ".", "jpg" ).getDelimiter(), "." );

    assertEquals( new FileName( "asdf", ".", "jpg" ).getName(), "asdf.jpg" );
    assertEquals( new FileName( "asdf", ".", "jpg" ).toString(), "asdf.jpg" );
  }

  @Test
  public void testCase() {
    assertEquals( new FileName( "asdf", ".", "jpg" ).getBaseName(), "asdf" );
    assertEquals( new FileName( "aSdf", ".", "JPG" ).getBaseName(), "aSdf" );

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
