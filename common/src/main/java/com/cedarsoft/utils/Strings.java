package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility method for string operations
 */
public class Strings {
  private Strings() {
  }

  @NotNull
  public static String stripQuotes( @NotNull String value ) {
    if ( value.indexOf( '\"' ) == 0 ) {
      value = value.substring( 1 );
    }

    if ( value.endsWith( "\"" ) ) {
      value = value.substring( 0, value.length() - 1 );
    }

    return value;
  }

  /**
   * Cuts the uncut to the given maxlength
   *
   * @param uncut     the uncut
   * @param maxLength the maxlength
   * @return the cut uncut
   */
  @NotNull
  public static String cut( @NotNull String uncut, int maxLength ) {
    if ( uncut.length() > maxLength ) {
      return uncut.substring( 0, maxLength );
    } else {
      return uncut;
    }
  }

  /**
   * Cuts the given uncut
   *
   * @param uncut     the uncut
   * @param maxLength the maxlength
   * @return the cut uncut or null if the given uncut has been null
   */
  @Nullable
  public static String cutNull( @Nullable String uncut, int maxLength ) {
    if ( uncut == null ) {
      return null;
    }
    return cut( uncut, maxLength );
  }
}
