package com.cedarsoft;

import org.apache.commons.io.FilenameUtils;
import org.testng.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class FileNameTest {
  @Test
  public void testDel() {
    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getBaseName(), "asdf" );
    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getExtension(), "jpg" );
    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getDelimiter(), "." );

    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getName(), "asdf.jpg" );
    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).toString(), "asdf.jpg" );
  }

  @Test
  public void testCase() {
    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getBaseName(), "asdf" );
    Assert.assertEquals( new FileName( "aSdf", ".", "JPG" ).getBaseName(), "aSdf" );

    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getExtension(), "jpg" );
    Assert.assertEquals( new FileName( "asdf", ".", "JPG" ).getExtension(), "JPG" );

    Assert.assertEquals( new FileName( "asdf", ".", "jpg" ).getDelimiter(), "." );
  }

  @Test
  public void testFileNameUtils() {
    assertEquals( FilenameUtils.getExtension( "asdf" ), "" );
    assertEquals( FilenameUtils.getExtension( "asdf." ), "" );

    assertEquals( FilenameUtils.getBaseName( "asdf." ), "asdf" );
  }
}
