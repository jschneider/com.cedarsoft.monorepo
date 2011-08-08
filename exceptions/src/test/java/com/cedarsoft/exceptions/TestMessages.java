package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TestMessages {
  @Nonnull
  private static final String BUNDLE_NAME = "com.cedarsoft.exceptions.testmessages"; //$NON-NLS-1$

  @Nonnull
  public String getString( @Nonnull String key ) {
    return getString( key, Locale.getDefault() );
  }

  @Nonnull
  public String getString( @Nonnull String key, @Nonnull Locale locale ) {
    return ResourceBundle.getBundle( BUNDLE_NAME, locale ).getString( key );
  }

  @Nonnull
  public String get( @Nonnull Enum<?> enumValue ) {
    return get( enumValue, Locale.getDefault() );
  }

  @Nonnull
  public String get( @Nonnull Enum<?> enumValue, @Nonnull Locale locale ) {
    return get( enumValue, null, locale );
  }

  @Nonnull
  public String get( @Nonnull Enum<?> enumValue, @Nullable String category ) {
    return get( enumValue, category, Locale.getDefault() );
  }

  public String get( @Nonnull Enum<?> enumValue, @Nullable String category, @Nonnull Locale locale ) {
    String baseKey = enumValue.name();

    String key;
    if ( category == null ) {
      key = baseKey;
    } else {
      key = baseKey + "." + category;
    }

    return getString( key, locale );
  }
}