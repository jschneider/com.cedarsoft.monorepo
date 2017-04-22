package com.cedarsoft.photos.di;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;

import javax.annotation.Nonnull;

/**
 * Offers methods related to DI
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Modules {
  private Modules() {
  }

  /**
   * Returns the modules
   */
  @Nonnull
  public static Iterable<? extends Module> getModules() {
    return ImmutableList.of(new StorageModule());
  }
}
