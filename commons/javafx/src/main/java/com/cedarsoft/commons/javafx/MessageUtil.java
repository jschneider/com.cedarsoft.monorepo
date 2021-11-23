package com.cedarsoft.commons.javafx;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Helper utility for messages
 */
public class MessageUtil {
  private MessageUtil() {
  }

  @Nonnull
  public static String get(@Nonnull ResourceBundle resourceBundle, @Nonnull String key) {
    return getString(resourceBundle, key);
  }

  @Nonnull
  public static String get(@Nonnull ResourceBundle resourceBundle, @Nonnull String key, @Nonnull Object... args) {
    return getString(resourceBundle, key, args);
  }

  @Nonnull
  public static String getString(@Nonnull ResourceBundle resourceBundle, @Nonnull String key) {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException ignore) {
      System.err.println("Can't find entry for key <" + key + "> in bundle " + resourceBundle.getBaseBundleName());
      return key;
    }
  }

  @Nonnull
  public static String getString(@Nonnull ResourceBundle resourceBundle, @Nonnull String key, @Nonnull Object... args) {
    return MessageFormat.format(getString(resourceBundle, key), args);
  }

  /**
   * Returns the resolved value or null if the key is null.
   */
  @Nullable
  public static String getStringNullable(@Nonnull ResourceBundle resourceBundle, @Nullable String key) throws MissingResourceException {
    if (key == null) {
      return null;
    }
    return resourceBundle.getString(key);
  }

  /**
   * Returns the resolved value or null if the key is null.
   */
  @Nullable
  public static String getStringNullable(@Nonnull ResourceBundle resourceBundle, @Nullable String key, @Nonnull Object... args) throws MissingResourceException {
    @Nullable String message = getStringNullable(resourceBundle, key);
    if (message == null) {
      return null;
    }
    return MessageFormat.format(message, args);
  }

  @Nonnull
  public static String get(@Nonnull ResourceBundle resourceBundle, @Nonnull Enum<?> enumValue) {
    return get(resourceBundle, enumValue, null);
  }

  @Nonnull
  public static String get(@Nonnull ResourceBundle resourceBundle, @Nonnull Enum<?> enumValue, @Nullable String category) {
    String key = getKey(enumValue, category);
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException ignore) {
      System.err.println("Can't find entry for key <" + enumValue.name() + "> in bundle " + resourceBundle.getBaseBundleName());
      return key;
    }
  }

  /**
   * Returns the key for the given enum value and category
   *
   * @param enumValue the enum value
   * @param category  the category
   * @return the key
   */
  @Nonnull
  public static String getKey(@Nonnull Enum<?> enumValue, @Nullable String category) throws MissingResourceException {
    String key;
    if (category == null) {
      key = enumValue.name();
    }
    else {
      key = enumValue.name() + "." + category;
    }
    return key;
  }


}
