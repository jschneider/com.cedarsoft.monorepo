package com.cedarsoft.async;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * is able to call callbacks with several parameters
 */
public interface CallbackCaller<T> {
  /**
   * Calls the callback
   *
   * @param callback the callback
   * @return the return value
   *
   * @throws Exception the exception that is thrown
   */
  @Nullable
  Object call( @NotNull T callback ) throws Exception;

  /**
   * Returns a description for the current callback caller.
   * Used for logging and debugging purposes.
   *
   * @return the description
   */
  @NotNull
  String getDescription();
}