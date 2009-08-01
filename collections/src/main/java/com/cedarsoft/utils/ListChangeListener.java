package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Is notified whenever a list changes its content.
 */
public interface ListChangeListener<T> extends EventListener {
  void elementAdded( int index, @NotNull T element );

  void elementRemoved( int index, @NotNull T element );
}
