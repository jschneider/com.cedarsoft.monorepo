/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.exceptions.handling;

import org.jetbrains.annotations.PropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Messages {
  private static final Logger LOG = LoggerFactory.getLogger(Messages.class.getName());

  @Nonnull
  private static final String BUNDLE_NAME = "com.cedarsoft.exceptions.handling.exception-messages";
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
