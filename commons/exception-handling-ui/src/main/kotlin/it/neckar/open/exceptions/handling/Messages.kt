/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package it.neckar.open.exceptions.handling

import org.jetbrains.annotations.PropertyKey
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle

/**
 */
object Messages {
  private val LOG = LoggerFactory.getLogger(Messages::class.java.name)

  private const val BUNDLE_NAME = "it.neckar.open.exceptions.handling.exception-messages"
  private val RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), Messages::class.java.classLoader)

  /**
   * Returns the value for the given key
   */
  @JvmStatic
  operator fun get(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String): String {
    return try {
      RESOURCE_BUNDLE.getString(key)
    } catch (ignore: MissingResourceException) {
      LOG.error("Key not found <" + RESOURCE_BUNDLE.javaClass.name + ": " + key + ">")
      key
    }
  }

  /**
   * Formats the resolved value for the given key
   */
  @JvmStatic
  operator fun get(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String, vararg args: Any): String {
    val message = get(key)
    return MessageFormat.format(message, *args)
  }

  /**
   * Returns the value or null if the key is null
   *
   * @param key the key
   */
  @JvmStatic
  fun getNullable(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String?): String? {
    return if (key == null) {
      null
    } else get(key)
  }

  @JvmStatic
  fun getNullable(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String?, vararg args: Any): String? {
    val message = getNullable(key) ?: return null
    return MessageFormat.format(message, *args)
  }

  @JvmStatic
  @JvmOverloads
  operator fun get(enumValue: Enum<*>, specifier: String? = null): String {
    return get(getKey(enumValue, specifier))
  }

  @JvmStatic
  @Throws(MissingResourceException::class)
  private fun getKey(enumValue: Enum<*>): String {
    return getKey(enumValue, null)
  }

  /**
   * Returns the key for a enum value and specifier
   */
  @JvmStatic
  @Throws(MissingResourceException::class)
  fun getKey(enumValue: Enum<*>, specifier: String?): String {
    return if (specifier == null) {
      enumValue.name
    } else enumValue.name + "." + specifier

  }
}
