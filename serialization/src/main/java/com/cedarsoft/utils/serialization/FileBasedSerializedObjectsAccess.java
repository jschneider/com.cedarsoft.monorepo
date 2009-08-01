package com.cedarsoft.utils.serialization;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.cedarsoft.utils.StillContainedException;

/**
 *
 */
public class FileBasedSerializedObjectsAccess implements RegistrySerializer.SerializedObjectsAccess {
  @NotNull
  @NonNls
  private final File baseDir;

  @NotNull
  @NonNls
  private final String extension;

  public FileBasedSerializedObjectsAccess( @NotNull File baseDir, @NotNull String extension ) {
    assert baseDir.exists();
    assert baseDir.isDirectory();

    this.baseDir = baseDir;
    this.extension = extension;
  }

  @NotNull
  public Set<? extends String> getStoredIds() throws FileNotFoundException {
    assert baseDir.exists();
    File[] files = baseDir.listFiles( ( FileFilter ) new SuffixFileFilter( extension ) );
    if ( files == null ) {
      throw new FileNotFoundException( "Could not list files in " + baseDir.getAbsolutePath() );
    }

    Set<String> ids = new HashSet<String>();
    for ( File file : files ) {
      ids.add( FilenameUtils.getBaseName( file.getName() ) );
    }

    return ids;
  }

  @NotNull
  public OutputStream openOut( @NotNull @NonNls String id ) throws FileNotFoundException {
    File file = getFile( id );
    if ( file.exists() ) {
      throw new StillContainedException( id );
    }
    return new BufferedOutputStream( new FileOutputStream( file ) );
  }

  @NotNull
  public InputStream getInputStream( @NotNull @NonNls String id ) throws FileNotFoundException {
    return new BufferedInputStream( new FileInputStream( getFile( id ) ) );
  }

  @NotNull
  private File getFile( @NotNull @NonNls String id ) {
    return new File( baseDir, id + '.' + extension );
  }

  @NotNull
  public File getBaseDir() {
    return baseDir;
  }
}
