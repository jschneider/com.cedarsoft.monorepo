package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An extended type of Boolean that has a third option: {@link #UNKNOWN}.
 */
public enum ExtendedBoolean {
  UNKNOWN(),
  TRUE(),
  FALSE();

  public boolean isTrue() {
    return this == TRUE;
  }

  public boolean isFalse() {
    return this == FALSE;
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }

  /**
   * Returns the description for the default locale
   *
   * @return the description for the default locale
   */
  @NotNull
  public String getDescription() {
    return getDescription( Locale.getDefault() );
  }

  /**
   * Returns the description for the given locale
   *
   * @param locale the locale
   * @return the description for the locale
   */
  @NotNull
  public String getDescription( @NotNull Locale locale ) {
    ResourceBundle bundle = ResourceBundle.getBundle( getClass().getName(), locale );
    return bundle.getString( name() );
  }

  /**
   * Returns the ExtendedBoolean for a boolean
   *
   * @param value the boolean value
   * @return {@link #TRUE} or {@link #FALSE} - depending on the given value
   */
  @NotNull
  public static ExtendedBoolean valueOf( boolean value ) {
    if ( value ) {
      return TRUE;
    } else {
      return FALSE;
    }
  }
}
