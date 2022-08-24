/**
 * Copyright (C) cedarsoft GmbH.

 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at

 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)

 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.

 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).

 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.app.xdg

import com.cedarsoft.common.kotlin.lang.checkNotNull
import java.io.File

/**
 * Created from http://standards.freedesktop.org/basedir-spec/basedir-spec-latest.html
 */
object XdgUtil {
  private val homeDir: File
    get() {
      val userHomeAsString = System.getProperty("user.home").checkNotNull { "No property found for user.home" }

      val home = File(userHomeAsString)
      if (!home.isDirectory) {
        throw IllegalStateException("User home <" + home.absolutePath + "> is not a directory")
      }
      return home
    }

  /**
   * The base directory relative to which user specific data files should be stored.
   * If $XDG_DATA_HOME is either not set or empty, a default equal to $HOME/.local/share should be used.
   */
  const val XDG_DATA_HOME: String = "XDG_DATA_HOME"

  /**
   * The base directory relative to which user specific configuration files should be stored.
   * If $XDG_CONFIG_HOME is either not set or empty, a default equal to $HOME/.config should be used.
   */
  const val XDG_CONFIG_HOME: String = "XDG_CONFIG_HOME"

  /**
   * The preference-ordered set of base directories to search for data files in addition to
   * the $XDG_DATA_HOME base directory.
   * The directories in $XDG_DATA_DIRS should be separated with a colon ':'.
   *
   * If $XDG_DATA_DIRS is either not set or empty, a value equal to /usr/local/share/:/usr/share/ should be used.
   */
  const val XDG_DATA_DIRS: String = "XDG_DATA_DIRS"

  /**
   * The preference-ordered set of base directories to search for configuration
   * files in addition to the $XDG_CONFIG_HOME base directory.
   * The directories in $XDG_CONFIG_DIRS should be seperated with a colon ':'.
   *
   * If $XDG_CONFIG_DIRS is either not set or empty, a value equal to /etc/xdg should be used.
   *
   * The order of base directories denotes their importance; the first directory listed is the most important.
   * When the same information is defined in multiple places the information defined relative to the more important base directory takes precedent.
   * The base directory defined by $XDG_DATA_HOME is considered more important than any of the base directories defined by $XDG_DATA_DIRS.
   * The base directory defined by $XDG_CONFIG_HOME is considered more important than any of the base directories defined by $XDG_CONFIG_DIRS.

   */
  const val XDG_CONFIG_DIRS: String = "XDG_CONFIG_DIRS"

  /**
   * The base directory relative to which user specific non-essential data files should be stored.
   * If $XDG_CACHE_HOME is either not set or empty, a default equal to $HOME/.cache should be used.
   */
  const val XDG_CACHE_HOME: String = "XDG_CACHE_HOME"

  /**
   * The base directory relative to which user-specific non-essential runtime files and other file objects (such as sockets, named pipes, ...) should be stored.
   * The directory MUST be owned by the user, and he MUST be the only one having read and write access to it. Its Unix access mode MUST be 0700.
   */
  const val XDG_RUNTIME_DIR: String = "XDG_RUNTIME_DIR"

  /**
   * Returns the data home dir
   */
  @JvmStatic
  val dataHome: File
    get() {
      val value = System.getenv()[XDG_DATA_HOME]
      return getDir(value, File(homeDir, ".local/share"))
    }

  /**
   * Returns the config home dir
   */
  @JvmStatic
  val configHome: File
    get() {
      val value = System.getenv()[XDG_CONFIG_HOME]
      return getDir(value, File(homeDir, ".config"))
    }

  /**
   * Returns the cache home dir
   */
  @JvmStatic
  val cacheHome: File
    get() {
      val value = System.getenv()[XDG_CACHE_HOME]
      return getDir(value, File(homeDir, ".cache"))
    }

  private fun getDir(dirName: String?, defaultDir: File): File {
    if (dirName == null) {
      return defaultDir
    }
    return File(dirName)
  }

}
