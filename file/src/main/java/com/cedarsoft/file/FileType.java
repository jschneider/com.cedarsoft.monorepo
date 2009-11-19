package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class FileType {

  @NotNull
  @NonNls
  private final List<Extension> extensions = new ArrayList<Extension>();

  @NotNull
  @NonNls
  private final String id;
  private final boolean dependentType;

  public FileType( @NotNull @NonNls String id, boolean dependentType, @NotNull @NonNls Extension... extensions ) {
    this( id, dependentType, Arrays.<Extension>asList( extensions ) );
  }

  public FileType( @NotNull @NonNls String id, boolean dependentType, @NotNull @NonNls Collection<? extends Extension> extensions ) {
    this.dependentType = dependentType;
    assert !extensions.isEmpty();
    this.id = id;
    this.extensions.addAll( extensions );
  }

  @NotNull
  public List<? extends Extension> getExtensions() {
    return Collections.unmodifiableList( extensions );
  }

  public boolean matches( @NotNull @NonNls String fileName ) {
    for ( @NotNull @NonNls Extension ex : extensions ) {
      if ( fileName.toLowerCase().endsWith( ex.getCombined() ) ) {
        return true;
      }
    }
    return false;
  }

  public boolean matches( @NotNull @NonNls FileName fileName ) {
    return matches( fileName.getName() );
  }

  @NotNull
  @NonNls
  public Extension getDefaultExtension() {
    if ( extensions.isEmpty() ) {
      throw new IllegalStateException( "Cannot return a default extension for " + this );
    }
    return extensions.get( 0 );
  }

  @NotNull
  public String getId() {
    return id;
  }

  /**
   * Returns whether this file is a dependent type.
   * A dependent file is a file that needs another file it depends on.
   *
   * @return whether this file is a dependent type
   */
  public boolean isDependentType() {
    return dependentType;
  }

  @NotNull
  public FileName getFileName( @NonNls @NotNull File file ) {
    return getFileName( file.getName() );
  }

  @NotNull
  public FileName getFileName( @NonNls @NotNull String fileName ) {
    @NonNls
    String bestBase = null;
    Extension bestExtension = null;

    for ( @NonNls Extension extension : extensions ) {
      int index = fileName.toLowerCase().indexOf( extension.getCombined() );
      if ( index < 0 ) {
        continue;
      }

      String base = fileName.substring( 0, index );
      if ( bestBase == null || base.length() < bestBase.length() ) {
        bestBase = base;
        bestExtension = extension;
      }
    }

    if ( bestBase == null ) {
      throw new IllegalArgumentException( "Cannot get base for " + fileName );
    }

    return new FileName( bestBase, bestExtension );
  }

  @NotNull
  @NonNls
  public Extension getExtension( @NonNls @NotNull String fileName ) {
    return getFileName( fileName ).getExtension();
  }

  @Deprecated
  @NotNull
  @NonNls
  public String getDelimiter( @NonNls @NotNull String fileName ) {
    return getFileName( fileName ).getDelimiter();
  }

  @NotNull
  @NonNls
  public String getBaseName( @NonNls @NotNull String fileName ) {
    return getFileName( fileName ).getBaseName().getName();
  }

  @Override
  public String toString() {
    return "FileType{" +
      "id='" + id + '\'' +
      '}';
  }

  public boolean isDefaultExtension( @NotNull Extension extension ) {
    return getDefaultExtension().equals( extension );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FileType ) ) return false;

    FileType fileType = ( FileType ) o;

    if ( dependentType != fileType.dependentType ) return false;
    if ( !extensions.equals( fileType.extensions ) ) return false;
    if ( !id.equals( fileType.id ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = extensions.hashCode();
    result = 31 * result + id.hashCode();
    result = 31 * result + ( dependentType ? 1 : 0 );
    return result;
  }
}
