package com.cedarsoft.commons.javafx

import java.text.MessageFormat
import java.util.MissingResourceException
import java.util.ResourceBundle

/**
 * Helper utility for messages
 */
object MessageUtil {
  @JvmStatic
  operator fun get(resourceBundle: ResourceBundle, key: String): String {
    return getString(resourceBundle, key)
  }

  @JvmStatic
  operator fun get(resourceBundle: ResourceBundle, key: String, vararg args: Any?): String {
    return getString(resourceBundle, key, *args)
  }

  @JvmStatic
  fun getString(resourceBundle: ResourceBundle, key: String): String {
    return try {
      resourceBundle.getString(key)
    } catch (ignore: MissingResourceException) {
      System.err.println("Can't find entry for key <" + key + "> in bundle " + resourceBundle.baseBundleName)
      key
    }
  }

  @JvmStatic
  fun getString(resourceBundle: ResourceBundle, key: String, vararg args: Any?): String {
    return MessageFormat.format(getString(resourceBundle, key), *args)
  }

  /**
   * Returns the resolved value or null if the key is null.
   */
  @JvmStatic
  @Throws(MissingResourceException::class)
  fun getStringNullable(resourceBundle: ResourceBundle, key: String?): String? {
    return if (key == null) {
      null
    } else resourceBundle.getString(key)
  }

  /**
   * Returns the resolved value or null if the key is null.
   */
  @JvmStatic
  @Throws(MissingResourceException::class)
  fun getStringNullable(resourceBundle: ResourceBundle, key: String?, vararg args: Any?): String? {
    val message = getStringNullable(resourceBundle, key) ?: return null
    return MessageFormat.format(message, *args)
  }

  @JvmStatic
  operator fun get(resourceBundle: ResourceBundle, enumValue: Enum<*>): String {
    return MessageUtil[resourceBundle, enumValue, null]
  }

  @JvmStatic
  operator fun get(resourceBundle: ResourceBundle, enumValue: Enum<*>, category: String?): String {
    val key = getKey(enumValue, category)
    try {
      return resourceBundle.getString(key)
    } catch (ignore: MissingResourceException) {
      System.err.println("Can't find entry for key <" + enumValue.name + "> in bundle " + resourceBundle.baseBundleName)
      return key
    }
  }

  /**
   * Returns the key for the given enum value and category
   *
   * @param enumValue the enum value
   * @param category  the category
   * @return the key
   */
  @JvmStatic
  @Throws(MissingResourceException::class)
  fun getKey(enumValue: Enum<*>, category: String?): String {
    return if (category == null) {
      enumValue.name
    } else {
      enumValue.name + "." + category
    }
  }
}
