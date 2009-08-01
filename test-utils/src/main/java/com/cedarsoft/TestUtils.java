package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

import java.io.File;
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
