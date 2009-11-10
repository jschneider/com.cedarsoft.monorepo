package com.cedarsoft.file;

import static org.testng.Assert.*;

import com.cedarsoft.file.FileName;
import com.cedarsoft.file.FileTypeRegistry;
import org.testng.annotations.*;

/**
 *
 */
public class FileTypeTest {
  @Test
  public void testGetFileName() {
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getExtension(), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getFileName( "asdf.jpg" ).getDelimiter(), "." );

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
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf.jpg" ), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpg" ), "jpg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.jpeg" ), "jpeg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "asdf_.JPEG" ), "jpeg" );
    assertEquals( FileTypeRegistry.JPEG.getExtension( "Asdf_.JPEG" ), "jpeg" );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf_lzn.jpg" ), "lzn.jpg" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__lzn.jpg" ), "lzn.jpg" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getExtension( "asdf__LZN.JPG" ), "lzn.jpg" );
  }

  @Test
  public void testGetDelimiter() {
    assertEquals( FileTypeRegistry.JPEG.getDelimiter( "asdf.jpg" ), "." );
    assertEquals( FileTypeRegistry.JPEG.getDelimiter( "asdf_.jpg" ), "." );
    assertEquals( FileTypeRegistry.JPEG.getDelimiter( "asdf_.jpeg" ), "." );
    assertEquals( FileTypeRegistry.JPEG.getDelimiter( "asdf_.JPEG" ), "." );
    assertEquals( FileTypeRegistry.JPEG.getDelimiter( "Asdf_.JPEG" ), "." );

    assertEquals( FileTypeRegistry.LIGHT_ZONE.getDelimiter( "asdf_lzn.jpg" ), "_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getDelimiter( "asdf__lzn.jpg" ), "_" );
    assertEquals( FileTypeRegistry.LIGHT_ZONE.getDelimiter( "asdf__LZN.JPG" ), "_" );
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
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getDelimiter(), "_" );
    assertEquals( fileTypeRegistry.get( "asdf_lzn.jpg" ).getFileName( "asdf_lzn.jpg" ).getExtension(), "lzn.jpg" );

    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getBaseName().getName(), "asdf" );
    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getDelimiter(), "_" );
    assertEquals( new FileName( "asdf", "_", "lzn.jpg" ).getExtension(), "lzn.jpg" );

    assertTrue( FileTypeRegistry.LIGHT_ZONE.matches( "my_lzn.jpg" ) );
  }

  @Test
  public void testDependentFiles() {
    assertFalse( FileTypeRegistry.GIMP.isDependentType() );
    assertFalse( FileTypeRegistry.JPEG.isDependentType() );
    assertTrue( FileTypeRegistry.LIGHT_ZONE.isDependentType() );
  }
}
