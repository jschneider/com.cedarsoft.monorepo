package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that creates messages to be printed to the console.
 * Depending on the implementation the messages may be formated differently.
 */
public interface ConsolePrinter {
  /**
   * Creates an error message
   *
   * @param message the raw message
   * @param objects the objects
   * @return the error message
   */
  @NotNull
  String createError( @NotNull String message, @NotNull Object... objects );

  /**
   * Creates a warning
   *
   * @param message the raw message
   * @param objects the objects
   * @return the warning message
   */
  @NotNull
  String createWarning( @NotNull String message, @NotNull Object... objects );

  /**
   * Creates a success message
   *
   * @param message the raw message
   * @param objects the objects
   * @return the success mesage
   */
  @NotNull
  String createSuccess( @NotNull String message, @NotNull Object... objects );
}