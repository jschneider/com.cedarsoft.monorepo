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

package com.cedarsoft.app


/**
 *
 * PdfViewerLauncher class.
 *
 */
object PdfViewerLauncher {
  @JvmStatic
  val osName: String
    get() = System.getProperty("os.name")

  /**
   * Opens a browser
   *
   * @param file the url that is opened within the browser
   */
  @JvmStatic
  fun openFile(file: String) {
    val osName = osName

    when {
      osName.startsWith("Mac OS") -> {
        throw UnsupportedOperationException("Not implemented for Mac OS yet")
        //        Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
        //        Method openFile = fileMgr.getDeclaredMethod( "openFile", new Class[]{String.class} );
        //        openFile.invoke( null, file );
      }
      osName.startsWith("Windows") -> {
        Runtime.getRuntime().exec("start \"$file\"")
      }
      else -> {
        //assume Unix or Linux
        val bins = arrayOf("acroread", "evince")

        var browser: String? = null

        var count = 0
        while (count < bins.size && browser == null) {
          if (Runtime.getRuntime().exec(arrayOf("which", bins[count])).waitFor() == 0) {
            browser = bins[count]
          }
          count++
        }

        if (browser == null) {
          error("Could not find pdf viewer")
        }

        Runtime.getRuntime().exec(arrayOf(browser, file))
      }
    }

  }
}
