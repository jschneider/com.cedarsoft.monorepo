package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Renders an object
 * T: The type of the object
 * C: The context type (optional)
 */
public interface Renderer<T, C> {
  /**
   * Renders an object
   *
   * @param object  the object
   * @param context the context
   * @return the rendered string
   */
  @NotNull
  String render( @NotNull T object, @Nullable C context );
}