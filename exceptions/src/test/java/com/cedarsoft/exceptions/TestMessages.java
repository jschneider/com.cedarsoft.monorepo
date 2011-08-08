package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TestMessages {
  @Nonnull
  private static final String BUNDLE_NAME = "com.cedarsoft.exceptions.testmessages"; //$NON-NLS-1$
  @Nonnull
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private TestMessages() {
  }

  @Nonnull
  public static String getString( @Nonnull String key ) {
    return RESOURCE_BUNDLE.getString( key );
  }

  @Nonnull
  public static String get( @Nonnull Enum<?> enumValue ) {
    return get( enumValue, null );
  }

  @Nonnull
  public static String get( @Nonnull Enum<?> enumValue, @Nullable String category ) {
    String baseKey = enumValue.name();

    String key;
    if ( category == null ) {
      key = baseKey;
    } else {
      key = baseKey + "." + category;
    }

    return getString( key );
  }
}