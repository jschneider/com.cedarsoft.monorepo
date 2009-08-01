package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Offers access to the application home dir.
 */
public interface ApplicationHomeAccess {
  /**
   * Returns the application name
   *
   * @return the application name
   */
  @NotNull
  String getApplicationName();

  /**
   * Returns the application home dir
   *
   * @return the application home dir
   */
  @NotNull
  File getApplicationHome();
}