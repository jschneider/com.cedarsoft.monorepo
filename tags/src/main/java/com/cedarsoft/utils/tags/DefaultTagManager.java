package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;

import java.lang.Override;

/**
 * The default implementation holds the tags in memory
 */
public class DefaultTagManager<T> extends MemoryTagManager<T> {
  @Override
  public void commit( @NotNull Taggable taggable ) {
    //do nothing - we have no database
  }
}
