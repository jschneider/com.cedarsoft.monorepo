package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents a file name
 */
public class FileName {
  @NonNls
  private static final String DEFAULT_DELIMITER = ".";

  @Nullable
  @NonNls
  private final String extension;
  @NotNull
  @NonNls
  private final BaseName baseName;
  @NonNls
  @Nullable
  private final String delimiter;

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the file extension
   */
  public FileName( @NonNls @NotNull String baseName, @NonNls @Nullable String extension ) {
    this( baseName, DEFAULT_DELIMITER, extension );
  }

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the extension
   */
  public FileName( @NonNls @NotNull String baseName, @NotNull Extension extension ) {
    this( baseName, extension.getDelimiter(), extension.getExtension() );
  }

  public FileName( @NonNls @NotNull String baseName, @NonNls @Nullable String delimiter, @NonNls @Nullable String extension ) {
    this( new BaseName( baseName ), delimiter, extension );
  }

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param delimiter the delimiter
   * @param extension the extension
   */
  public FileName( @NonNls @NotNull BaseName baseName, @NonNls @Nullable String delimiter, @NonNls @Nullable String extension ) {
    this.baseName = baseName;
    this.delimiter = delimiter;
    this.extension = extension;
  }

  @NotNull
  public BaseName getBaseName() {
    return baseName;
  }

  @NonNls
  @Nullable
  public String getExtension() {
    return extension;
  }

  @NonNls
  @NotNull
  public String getExtensionNonNull() {
    if ( extension == null ) {
      return "";
    }
    return extension;
  }

  @NotNull
  @NonNls
  public String getName() {
    return baseName + delimiter + extension;
  }

  @Nullable
  @NonNls
  public String getDelimiter() {
    return delimiter;
  }

  @NotNull
  @NonNls
  public String getDelimiterNonNull() {
    if ( delimiter == null ) {
      return "";
    }
    return delimiter;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FileName ) ) return false;

    FileName fileName = ( FileName ) o;

    if ( !baseName.equals( fileName.baseName ) ) return false;
    if ( delimiter != null ? !delimiter.equals( fileName.delimiter ) : fileName.delimiter != null ) return false;
    if ( extension != null ? !extension.equals( fileName.extension ) : fileName.extension != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = extension != null ? extension.hashCode() : 0;
    result = 31 * result + baseName.hashCode();
    result = 31 * result + ( delimiter != null ? delimiter.hashCode() : 0 );
    return result;
  }

  @Override
  public String toString() {
    if ( extension == null ) {
      return baseName.toString();
    }
    return getName();
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
