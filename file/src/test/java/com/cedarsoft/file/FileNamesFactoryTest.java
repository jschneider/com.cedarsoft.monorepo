package com.cedarsoft.file;

import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FileNamesFactoryTest {
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private FileTypeRegistry fileTypeRegistry;
  private FileNamesFactory factory;
  private File baseDir;

  @Before
  public void setUp() throws Exception {
    fileTypeRegistry = new FileTypeRegistry( true );
    factory = new FileNamesFactory( fileTypeRegistry );
    baseDir = folder.newFolder( "myFolder" );
  }

  @Test
  public void testBaseNameAware() throws Exception {
    File f1 = new File( baseDir, "A.JPG" );
    f1.createNewFile();
    File f2 = new File( baseDir, "A.cr2" );
    f2.createNewFile();

    BaseNameAwareFileNames baseNameAware = factory.createBaseNameAware( baseDir );
    assertThat( baseNameAware ).isNotNull();
    assertThat( baseNameAware.getEntries() ).hasSize( 1 );
    Map.Entry<BaseName, FileNames> entry = baseNameAware.getEntries().iterator().next();
    assertThat( entry.getKey().getName() ).isEqualTo( "A" );
    assertThat( entry.getValue().getFileNames() ).hasSize( 2 );
    assertThat( entry.getValue().getFileNames().get( 0 ).getName() ).isEqualTo( "A.JPG" );
    assertThat( entry.getValue().getFileNames().get( 1 ).getName() ).isEqualTo( "A.cr2" );

    for ( FileName fileName : entry.getValue().getFileNames() ) {
      File f = new File( baseDir, fileName.getName() );
      assertThat( f ).exists();
    }
  }

  @Test
  public void testLowerCase() throws Exception {
    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).isEmpty();
    }

    File f1 = new File( baseDir, "a.jpg" );
    f1.createNewFile();
    File f2 = new File( baseDir, "a.cr2" );
    f2.createNewFile();

    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).hasSize( 2 );

      assertThat( fileNames.getFileNames().get( 0 ).getName() ).isEqualTo( "a.jpg" );
      assertThat( fileNames.getFileNames().get( 1 ).getName() ).isEqualTo( "a.cr2" );
    }
  }

  @Test
  public void testMixedCase() throws Exception {
    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).isEmpty();
    }

    File f1 = new File( baseDir, "A.JPG" );
    f1.createNewFile();
    File f2 = new File( baseDir, "A.cr2" );
    f2.createNewFile();

    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).hasSize( 2 );

      assertThat( fileNames.getFileNames().get( 0 ).getName() ).isEqualTo( "A.JPG" );
      assertThat( fileNames.getFileNames().get( 1 ).getName() ).isEqualTo( "A.cr2" );
    }
  }

  @Test
  public void testUpperCase() throws Exception {
    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).isEmpty();
    }

    File f1 = new File( baseDir, "A.JPG" );
    f1.createNewFile();
    File f2 = new File( baseDir, "A.CR2" );
    f2.createNewFile();

    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).hasSize( 2 );

      assertThat( fileNames.getFileNames().get( 0 ).getName() ).isEqualTo( "A.JPG" );
      assertThat( fileNames.getFileNames().get( 1 ).getName() ).isEqualTo( "A.CR2" );
    }
  }
}
