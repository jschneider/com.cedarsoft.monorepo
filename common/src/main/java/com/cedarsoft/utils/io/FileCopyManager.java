package com.cedarsoft.utils.io;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * Offers utility methods for file copying
 */
public class FileCopyManager {
  private FileCopyManager() {
  }

  public static void deleteForced( @NotNull File toDelete ) {
    if ( !toDelete.exists() ) {
      throw new IllegalArgumentException( "File must exist: " + toDelete.getAbsolutePath() );
    }

    if ( toDelete.isDirectory() ) {
      for ( String entry : toDelete.list() ) {
        File child = new File( toDelete, entry );
        deleteForced( child );
      }
    }
    toDelete.delete();
  }

  public static void copy( @NotNull File src, @NotNull File dest ) throws IOException {
    if ( src.isDirectory() ) {
      copyDirectory( src, dest );
    } else {
      copyFile( src, dest );
    }
  }

  public static void copyFile( @NotNull File source, @NotNull File target ) throws IOException {
    FileChannel sourceChannel = null;
    FileChannel targetChannel = null;
    FileInputStream in = null;
    FileOutputStream out = null;
    try {
      in = new FileInputStream( source );
      sourceChannel = in.getChannel();
      out = new FileOutputStream( target );
      targetChannel = out.getChannel();
      sourceChannel.transferTo( 0, sourceChannel.size(), targetChannel );
    } finally {
      if ( sourceChannel != null ) {
        sourceChannel.close();
      }
      if ( targetChannel != null ) {
        targetChannel.close();
      }
      if ( in != null ) {
        in.close();
      }
      if ( out != null ) {
        out.close();
      }
    }
  }

  public static void copyDirectory( @NotNull File srcDir, @NotNull File destDir ) throws IOException {
    if ( !destDir.exists() ) {
      destDir.mkdirs();
    }

    for ( String entry : srcDir.list() ) {
      File src = new File( srcDir, entry );
      File dest = new File( destDir, entry );
      copy( src, dest );
    }
  }
}
