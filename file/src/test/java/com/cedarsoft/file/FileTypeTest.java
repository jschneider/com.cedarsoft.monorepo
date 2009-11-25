package com.cedarsoft.file;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class FileTypeTest {
  @Test
  public void testGetFileName() {
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension().getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension().getDelimiter(), "." );

    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpeg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.JPEG" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "Asdf_.JPEG" ), "Asdf_" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf_lzn.jpg" ), "asdf" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__lzn.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__LZN.JPG" ), "asdf_" );
  }

  @Test
  public void testGetBase() {
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf.jpg" ), "asdf" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.jpeg" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "asdf_.JPEG" ), "asdf_" );
    assertEquals( FileTypeRegistry.JPEG.getBaseName( "Asdf_.JPEG" ), "Asdf_" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf_lzn.jpg" ), "asdf" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__lzn.jpg" ), "asdf_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getBaseName( "asdf__LZN.JPG" ), "asdf_" );
  }

  @Test
  public void testGetExtension() {
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ).getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ).getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ).getExtension(), "jpeg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ).getExtension(), "jpeg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ).getExtension(), "jpeg" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ).getExtension(), "lzn.jpg" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ).getExtension(), "lzn.jpg" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ).getExtension(), "lzn.jpg" );
  }

  @Test
  public void testGetDelimiter() {
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ).getDelimiter(), "." );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ).getDelimiter(), "." );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ).getDelimiter(), "_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ).getDelimiter(), "_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ).getDelimiter(), "_" );
  }

  @Test
  public void testExtension() {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertSame( fileTypeRegistry.get( new FileName( "asdf", ".", "jpg" ) ), FileTypeRegistry.JPEG );
    assertSame( fileTypeRegistry.get( new FileName( "asdf", ".", "cr2" ) ), FileTypeRegistry.RAW_CANON );

    assertSame( fileTypeRegistry.get( "asdf.jpg" ), FileTypeRegistry.JPEG );
    assertSame( fileTypeRegistry.get( "asdf.cr2" ), FileTypeRegistry.RAW_CANON );
    assertSame( fileTypeRegistry.get( "asdf_lzn.jpg" ), FileTypeRegistry.LIGHT_ZONE );
  }

  @Test
  public void testLightZone() {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ), FileTypeRegistry.LIGHT_ZONE );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension().getDelimiter(), "_" );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension().getExtension(), "lzn.jpg" );

    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getExtension().getDelimiter(), "_" );
    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getExtension().getExtension(), "lzn.jpg" );

    assertTrue( FileTypeRegistry.LIGHT_ZONE.matches( "my_lzn.jpg" ) );
  }

  @Test
  public void testDependentFiles() {
    assertFalse( FileTypeRegistry.GIMP.isDependentType() );
    assertFalse( FileTypeRegistry.JPEG.isDependentType() );
    assertTrue( FileTypeRegistry.LIGHT_ZONE.isDependentType() );
  }
}
