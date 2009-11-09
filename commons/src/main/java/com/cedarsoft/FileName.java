package com.cedarsoft;

import com.cedarsoft.Extension;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  private final String baseName;
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

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param delimiter the delimiter
   * @param extension the extension
   */
  public FileName( @NonNls @NotNull String baseName, @NonNls @Nullable String delimiter, @NonNls @Nullable String extension ) {
    this.baseName = baseName;
    this.delimiter = delimiter;
    this.extension = extension;
  }

  @NonNls
  @NotNull
  public String getBaseName() {
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
    if ( !delimiter.equals( fileName.delimiter ) ) return false;
    if ( extension != null ? !extension.equals( fileName.extension ) : fileName.extension != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = extension != null ? extension.hashCode() : 0;
    result = 31 * result + baseName.hashCode();
    result = 31 * result + delimiter.hashCode();
    return result;
  }

  @Override
  public String toString() {
    if ( extension == null ) {
      return baseName;
    }
    return getName();
  }
}
