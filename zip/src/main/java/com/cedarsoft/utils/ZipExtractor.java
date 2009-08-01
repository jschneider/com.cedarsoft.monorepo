package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracts ZIP files
 */
public class ZipExtractor {
  private static final int BUFFER_LENGTH = 1024;
  private final File zipFile;
  private Condition condition;

  /**
   * Creates a new extractor for the given file
   *
   * @param zipFile the zip file that will be extracted
   * @throws FileNotFoundException if the file does not exist
   */
  public ZipExtractor( @NotNull File zipFile ) throws FileNotFoundException {
    if ( !zipFile.exists() ) {
      throw new FileNotFoundException( "File not found " + zipFile.getAbsolutePath() );
    }
    this.zipFile = zipFile;
  }

  /**
   * Extract the zip file to the given destination
   *
   * @param destination the destination the file will be extracted to
   * @throws IOException
   */
  public void extract( @NotNull File destination ) throws IOException {
    if ( !destination.exists() || !destination.isDirectory() ) {
      throw new IllegalArgumentException( "Invalid destination: " + destination.getCanonicalPath() );
    }

    ZipInputStream zipInputStream = null;
    try {
      zipInputStream = new ZipInputStream( new FileInputStream( zipFile ) );

      byte[] buf = new byte[BUFFER_LENGTH];
      for ( ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry() )
      {
        if ( condition != null && !condition.shallExtract( zipEntry ) ) {
          continue;
        }

        String entryName = zipEntry.getName();
        File newFile = new File( destination, entryName );

        //Is a directory
        if ( zipEntry.isDirectory() ) {
          newFile.mkdirs();
          continue;
        }

        //Make the directory structure
        newFile.getParentFile().mkdirs();

        FileOutputStream fileoutputstream = null;
        try {
          fileoutputstream = new FileOutputStream( newFile );

          int n;
          while ( ( n = zipInputStream.read( buf, 0, BUFFER_LENGTH ) ) > -1 ) {
            fileoutputstream.write( buf, 0, n );
          }
        } finally {
          if ( fileoutputstream != null ) {
            fileoutputstream.close();
          }
        }
      }
    } finally {
      if ( zipInputStream != null ) {
        zipInputStream.close();
      }
    }
  }

  /**
   * Returns the currently set condition
   *
   * @return the condition
   */
  @Nullable
  public Condition getCondition() {
    return condition;
  }

  /**
   * Sets the (optional) condition
   *
   * @param condition the condition
   */
  public void setCondition( @Nullable Condition condition ) {
    this.condition = condition;
  }

  /**
   * A condition can decide whether a given zip entry shall be extracted or not
   */
  public interface Condition {
    /**
     * Returns whether the given entry shall be extracted
     *
     * @param zipEntry the zip entry
     * @return whether the entry shall be extracted
     */
    boolean shallExtract( @NotNull ZipEntry zipEntry );
  }

  /**
   * Inverts a condition
   */
  public static class InvertedCondition implements Condition {
    private final Condition condition;

    /**
     * Creates a new inverted condition
     *
     * @param condition the delegate that is inverted
     */
    public InvertedCondition( @NotNull Condition condition ) {
      this.condition = condition;
    }

    public boolean shallExtract( @NotNull ZipEntry zipEntry ) {
      return !condition.shallExtract( zipEntry );
    }
  }
}
