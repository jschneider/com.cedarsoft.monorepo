package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

/**
 * Helper class that creates zip files
 */
public class ZipCreator {
  static final int BUFFER_SIZE = 2048;

  @NotNull
  private final File zipFile;

  /**
   * Create a new zip creator with the given file as backend
   *
   * @param zipFile the zip file
   */
  public ZipCreator( @NotNull File zipFile ) {
    this.zipFile = zipFile;
  }

  /**
   * Returns the zip file
   *
   * @return the zip file
   */
  @NotNull
  public File getZipFile() {
    return zipFile;
  }

  /**
   * Zip all files within the given directories
   *
   * @param directories the directories all files are zipped within
   * @return the zipped file
   *
   * @throws IOException if an io exception occures
   */
  public File zip( @NotNull File... directories ) throws IOException {
    ZipOutputStream outStream = new ZipOutputStream( new BufferedOutputStream( new FileOutputStream( zipFile ) ) );
    try {
      for ( File directory : directories ) {
        String baseName = directory.getCanonicalPath();
        addFiles( baseName, outStream, directory );
      }
    } finally {
      outStream.close();
    }
    return zipFile;
  }

  /**
   * Add the files within the given directory to the ZipOutputStream. This method will call itself for each subdirectory.
   *
   * @param baseName  represents the relative base name within the zip file
   * @param outStream the ouput stream
   * @param directory the directory
   * @throws IOException
   */
  protected void addFiles( @NotNull String baseName, @NotNull ZipOutputStream outStream, @NotNull File directory ) throws IOException {
    byte[] data = new byte[BUFFER_SIZE];
    for ( File file : directory.listFiles() ) {
      String relativeName = getRelativePath( baseName, file );
      ZipEntry entry = new ZipEntry( relativeName );
      try {
        outStream.putNextEntry( entry );
      } catch ( ZipException ignore ) {
      }

      if ( file.isDirectory() ) {
        //Add the files within the directory
        addFiles( baseName, outStream, file );
        continue;
      }

      FileInputStream fileInputStream = null;
      BufferedInputStream origin = null;
      try {
        fileInputStream = new FileInputStream( file );
        origin = new BufferedInputStream( fileInputStream, BUFFER_SIZE );
        int count;
        while ( ( count = origin.read( data, 0, BUFFER_SIZE ) ) != -1 ) {
          outStream.write( data, 0, count );
        }
      } finally {
        if ( fileInputStream != null ) {
          fileInputStream.close();
        }
        if ( origin != null ) {
          origin.close();
        }
      }
    }
  }

  /**
   * Returns the relative path
   *
   * @param baseName the base path
   * @param file     the file
   * @return the path of the given file relative to the base name
   *
   * @throws IOException
   */
  @NotNull
  protected static String getRelativePath( @NotNull String baseName, @NotNull File file ) throws IOException {
    //noinspection NonConstantStringShouldBeStringBuffer
    String name = file.getCanonicalPath().substring( baseName.length() + 1 );
    if ( file.isDirectory() ) {
      name += '/';
    }
    return name.replaceAll( "\\\\", "/" );
  }
}
