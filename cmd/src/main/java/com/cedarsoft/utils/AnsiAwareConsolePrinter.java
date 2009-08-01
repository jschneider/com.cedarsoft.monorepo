package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * A console printer that is ansi aware
 */
public class AnsiAwareConsolePrinter implements ConsolePrinter {
  private static final char ANSI_ESCAPE = ( char ) 27;
  @NotNull
  @NonNls
  private static final String ANSI_BLUE = ANSI_ESCAPE + "[1;34m";
  @NotNull
  @NonNls
  private static final String ANSI_RED = ANSI_ESCAPE + "[1;31m";
  @NotNull
  @NonNls
  private static final String ANSI_GREEN = ANSI_ESCAPE + "[1;32m";
  @NotNull
  @NonNls
  private static final String ANSI_DEFAULT = ANSI_ESCAPE + "[0m";

  @NotNull
  public String createError( @NotNull String message, @NotNull Object... objects ) {
    return ANSI_RED + MessageFormat.format( message, objects ) + ANSI_DEFAULT;
  }

  @NotNull
  public String createWarning( @NotNull String message, @NotNull Object... objects ) {
    return ANSI_BLUE + MessageFormat.format( message, objects ) + ANSI_DEFAULT;
  }

  @NotNull
  public String createSuccess( @NotNull String message, @NotNull Object... objects ) {
    return ANSI_GREEN + MessageFormat.format( message, objects ) + ANSI_DEFAULT;
  }
}
