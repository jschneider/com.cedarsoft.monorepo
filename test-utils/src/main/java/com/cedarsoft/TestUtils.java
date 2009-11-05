package com.cedarsoft;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 */
public class TestUtils {
  private TestUtils() {
  }

  /**
   * Cleans up all fields within a given test
   *
   * @param test the test that is cleaned up
   */
  public static void cleanupFields( Object test ) throws IllegalAccessException {
    if ( test == null ) {
      return;
    }

    for ( Field field : test.getClass().getDeclaredFields() ) {
      if ( Modifier.isFinal( field.getModifiers() ) ) {
        continue;
      }
      field.setAccessible( true );
      field.set( test, null );
    }
  }

  @NotNull
  public static File getTmpDir() {
    return new File( System.getProperty( "java.io.tmpdir" ) );
  }

  @NotNull
  public static File createTmpFile( @NotNull @NonNls String prefix, @NotNull @NonNls String suffix, @NotNull InputStream in ) throws IOException {
    File file = File.createTempFile( prefix, suffix );
    file.deleteOnExit();

    FileOutputStream out = new FileOutputStream( file );
    try {
      IOUtils.copy( in, out );
    } finally {
      out.close();
    }
    return file;
  }

  @NotNull
  public static File createEmptyTmpDir() {
    File tmp = getTmpDir();
    File dir = null;

    while ( dir == null || dir.exists() ) {
      dir = new File( tmp, String.valueOf( ( int ) ( Math.random() * 1000000 ) ) );
    }

    dir.mkdir();
    return dir;
  }
}
