package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents a file name
 */
public class FileName {
  @NotNull
  @NonNls
  private final BaseName baseName;
  @NotNull
  @NonNls
  private final Extension extension;

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the file extension
   */
  public FileName( @NonNls @NotNull String baseName, @NonNls @NotNull String extension ) {
    this( new BaseName( baseName ), new Extension( extension ) );
  }

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the extension
   */
  public FileName( @NonNls @NotNull String baseName, @NotNull Extension extension ) {
    this( new BaseName( baseName ), extension );
  }

  public FileName( @NonNls @NotNull String baseName, @NonNls @NotNull String delimiter, @NonNls @NotNull String extension ) {
    this( new BaseName( baseName ), new Extension( delimiter, extension ) );
  }

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param delimiter the delimiter
   * @param extension the extension
   */
  public FileName( @NonNls @NotNull BaseName baseName, @NonNls @NotNull String delimiter, @NonNls @NotNull String extension ) {
    this( baseName, new Extension( delimiter, extension ) );
  }

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param extension the extension
   */
  public FileName( @NonNls @NotNull BaseName baseName, @NotNull Extension extension ) {
    this.baseName = baseName;
    this.extension = extension;
  }

  @NotNull
  public BaseName getBaseName() {
    return baseName;
  }

  @NonNls
  @NotNull
  public Extension getExtension() {
    return extension;
  }

  @NotNull
  @NonNls
  public String getName() {
    return baseName + extension.getCombined();
  }

  @Deprecated
  @NotNull
  @NonNls
  public String getDelimiter() {
    return getExtension().getDelimiter();
  }

  @Deprecated
  @NotNull
  @NonNls
  public String getDelimiterNonNull() {
    return getDelimiter();
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FileName ) ) return false;

    FileName fileName = ( FileName ) o;

    if ( !baseName.equals( fileName.baseName ) ) return false;
    if ( !extension.equals( fileName.extension ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = baseName.hashCode();
    result = 31 * result + extension.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return baseName.toString() + extension.toString();
  }

  /**
   * Returns the corresponding file
   *
   * @param baseDir the base dir
   * @return the file represented
   */
  @NotNull
  public File getFile( @NotNull File baseDir ) {
    assert baseDir.isDirectory();
    return new File( baseDir, getName() );
  }
}
