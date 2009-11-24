package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface ObjectCommit<T> {
  /**
   * Commit changes
   *
   * @param element the element that has been changed
   */
  void commit( @NotNull T element );
}
