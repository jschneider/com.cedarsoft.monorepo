/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.file;

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
