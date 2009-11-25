package com.cedarsoft.file;

import org.testng.annotations.*;

import java.util.Collections;
import java.util.Comparator;

import static org.testng.Assert.*;

/**
 *
 */
public class FileTypeRegistryTest {
  @Test
  public void testParse() {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertEquals( fileTypeRegistry.parseFileName( "asdf.jpg" ), new FileName( "asdf", ".", "jpg" ) );
  }

  @Test
  public void testIt() {
    FileTypeRegistry registry = new FileTypeRegistry( false );

    registry.store( new FileType( "Canon Raw", false, new Extension( ".", "cr2" ) ) );
    registry.store( new FileType( "Photoshop", false, new Extension( ".", "psd" ) ) );

    assertEquals( registry.getStoredObjects().size(), 2 );
    assertEquals( registry.getFileTypes().size(), 2 );
  }

  @Test
  public void testDefaults() {
    FileTypeRegistry registry = new FileTypeRegistry( Collections.<FileType>emptyList(), new Comparator<FileType>() {
      @Override
      public int compare( FileType o1, FileType o2 ) {
        return o1.getId().compareTo( o2.getId() );
      }
    } );

    assertEquals( registry.getStoredObjects().size(), 0 );
    registry.ensureDefaultTypesRegistered();
    assertEquals( registry.getStoredObjects().size(), 6 );
  }
}
