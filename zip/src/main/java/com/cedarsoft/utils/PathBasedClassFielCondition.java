package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.zip.ZipEntry;

/**
 * Condition for class files that is based on a path prefix.
 */
public class PathBasedClassFielCondition implements ZipExtractor.Condition {
  @NonNls
  @NotNull
  private static final String CLASS_SUFFIX = ".class";

  @NotNull
  @NonNls
  private String pathPrefix;

  /**
   * Creates a new condition
   *
   * @param pathPrefix the path prefix that is used
   */
  public PathBasedClassFielCondition( @NotNull @NonNls String pathPrefix ) {
    this.pathPrefix = pathPrefix;
  }

  public boolean shallExtract( @NotNull ZipEntry zipEntry ) {
    return zipEntry.getName().startsWith( pathPrefix ) && zipEntry.getName().endsWith( CLASS_SUFFIX );
  }
}
