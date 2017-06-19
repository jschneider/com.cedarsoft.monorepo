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

package com.cedarsoft.app

import com.cedarsoft.app.xdg.WindowsUtil
import com.cedarsoft.app.xdg.XdgUtil
import java.io.File

/**
 * Offers access to the application home dir.

 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
interface ApplicationHomeAccess {
  /**
   * Returns the application name
   */
  val applicationName: String

  /**
   * Returns the directory that should be used for the configuration
   */
  val configHome: File

  /**
   * Returns the directory that should be used for the user specific data
   */
  val dataHome: File

  /**
   * Returns the directory that should be used for caches
   */
  val cacheHome: File
}

/**
 * Factory for application home access
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object ApplicationHomeAccessFactory {
  @JvmStatic
  fun create(applicationName: String): ApplicationHomeAccess {
    val osName = System.getProperty("os.name") ?: throw IllegalStateException("Property os.name not found")

    if (osName.contains("Linux")) {
      return createLinuxHomeAccess(applicationName)
    }

    if (osName.contains("Windows")) {
      return createWindowsHomeAccess(applicationName)
    }

    throw IllegalStateException("Unsupported OS: " + osName)
  }

  @JvmStatic
  private fun createWindowsHomeAccess(applicationName: String): ApplicationHomeAccess {
    val appData = File(WindowsUtil.getAppData(), applicationName)
    createDirIfNecessary(appData)
    val localAppData = File(WindowsUtil.getLocalAppData(), applicationName)
    createDirIfNecessary(localAppData)
    return StaticApplicationHomeAccess(applicationName, appData, appData, localAppData)
  }


  @JvmStatic
  private fun createLinuxHomeAccess(applicationName: String): ApplicationHomeAccess {
    val configHome = File(XdgUtil.configHome, applicationName)
    createDirIfNecessary(configHome)
    val dataHome = File(XdgUtil.dataHome, applicationName)
    createDirIfNecessary(dataHome)
    val cacheHome = File(XdgUtil.cacheHome, applicationName)
    createDirIfNecessary(cacheHome)

    return StaticApplicationHomeAccess(applicationName, configHome, dataHome, cacheHome)
  }

  /**
   * Creates a app home access within the temp dir
   */
  @JvmStatic
  fun createTemporaryApplicationHomeAccess(): ApplicationHomeAccess {
    val dir = File(File(System.getProperty("java.io.tmpdir")), "." + System.currentTimeMillis())
    return createTemporaryApplicationHomeAccess(dir)
  }

  @JvmStatic
  fun createTemporaryApplicationHomeAccess(dir: File): ApplicationHomeAccess {
    val configHome = File(dir, "config")
    createDirIfNecessary(configHome)
    val data = File(dir, "data")
    createDirIfNecessary(data)
    val cacheHome = File(dir, "cache")
    createDirIfNecessary(cacheHome)

    return StaticApplicationHomeAccess("mockDir", configHome, data, cacheHome)
  }

  @JvmStatic
  private fun createDirIfNecessary(dir: File) {
    if (dir.isDirectory) {
      return
    }

    if (dir.isFile) {
      throw IllegalStateException(dir.absolutePath + " is a file")
    }

    if (dir.exists()) {
      throw IllegalStateException(dir.absolutePath + " still exists but is not a dir")
    }

    if (!dir.mkdirs()) {
      throw IllegalStateException("Could not create directory <" + dir.absolutePath + ">")
    }
  }
}