package com.cedarsoft.swing.common;

import org.jetbrains.annotations.PropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
  private static final Logger LOG = LoggerFactory.getLogger(Messages.class.getName());

  @Nonnull
  private static final String BUNDLE_NAME = "com.cedarsoft.swing.common.swing-common-messages";
  @Nonnull
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), Messages.class.getClassLoader());

  private Messages() {
  }

  /**
   * Returns the value for the given key
   */
  @Nonnull
  public static String get(@PropertyKey(resourceBundle = BUNDLE_NAME) @Nonnull String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException ignore) {
      LOG.error("Key not found <" + RESOURCE_BUNDLE.getClass().getName() + ": " + key + ">");
      return key;
    }
  }

  /**
   * Formats the resolved value for the given key
   */
  @Nonnull
  public static String get(@PropertyKey(resourceBundle = BUNDLE_NAME) @Nonnull String key, @Nonnull Object... args) {
    @Nonnull String message = get(key);
    return MessageFormat.format(message, args);
  }

  /**
   * Returns the value or null if the key is null
   *
   * @param key the key
   */
  @Nullable
  public static String getNullable(@PropertyKey(resourceBundle = BUNDLE_NAME) @Nullable String key) {
    if (key == null) {
      return null;
    }
    return get(key);
  }

  @Nullable
  public static String getNullable(@PropertyKey(resourceBundle = BUNDLE_NAME) @Nullable String key, @Nonnull Object... args) {
    @Nullable
    String message = getNullable(key);
    if (message == null) {
      return null;
    }
    return MessageFormat.format(message, args);
  }

  @Nonnull
  public static String get(@Nonnull Enum<?> enumValue) {
    return get(enumValue, null);
  }

  @Nonnull
  public static String get(@Nonnull Enum<?> enumValue, @Nullable String specifier) {
    return get(getKey(enumValue, specifier));
  }

  @Nonnull
  private static String getKey(@Nonnull Enum<?> enumValue) throws MissingResourceException {
    return getKey(enumValue, null);
  }

  /**
   * Returns the key for a enum value and specifier
   */
  @Nonnull
  private static String getKey(@Nonnull Enum<?> enumValue, @Nullable String specifier) throws MissingResourceException {
    if (specifier == null) {
      return enumValue.name();
    }

    return enumValue.name() + "." + specifier;
  }
}
