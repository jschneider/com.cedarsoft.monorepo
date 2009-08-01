package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;

/**
 * The default implementation holds the tags in memory
 */
public class DefaultTagManager<T> extends MemoryTagManager<T> {
  public void commit( @NotNull Taggable taggable ) {
    //do nothing - we have no database
  }
}
