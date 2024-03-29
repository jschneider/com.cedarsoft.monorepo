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

package it.neckar.open.app


import java.io.File
import java.io.IOException
import java.util.Collections

/**
 *
 * OfficeLauncher class.
 *
 */
object OfficeLauncher {
  const val OPEN_OFFICE_WINDOWS_2_3 = "c:\\Programme\\Openoffice.org 2.3\\program\\soffice.exe"
  const val OPEN_OFFICE_WINDOWS = "c:\\Programme\\Openoffice.org 2.4\\program\\soffice.exe"
  const val OPEN_OFFICE_LINUX = "/usr/bin/soffice"
  const val EXCEL_WINDOWS = "c:\\Programme\\Microsoft Office\\Office\\excel.exe"

  private val writerBins = ArrayList<String>()
  private val spreadsheetBins = ArrayList<String>()

  init {
    writerBins.add(OPEN_OFFICE_LINUX)
    writerBins.add(OPEN_OFFICE_WINDOWS)
    writerBins.add(OPEN_OFFICE_WINDOWS_2_3)

    spreadsheetBins.add(OPEN_OFFICE_LINUX)
    spreadsheetBins.add(OPEN_OFFICE_WINDOWS)
    spreadsheetBins.add(OPEN_OFFICE_WINDOWS_2_3)
    spreadsheetBins.add(EXCEL_WINDOWS)
  }

  @JvmStatic
  fun getSpreadsheetBins(): List<String> {
    return Collections.unmodifiableList(spreadsheetBins)
  }

  @JvmStatic
  fun getWriterBins(): List<String> {
    return Collections.unmodifiableList(writerBins)
  }

  /**
   *
   * openWriter
   *
   * @param file a File object.
   * @return a Process object.
   *
   * @throws IOException if any.
   */
  @JvmStatic
  fun openWriter(file: File): Process {
    return openFile(writerBins, file)
  }

  /**
   *
   * openSpreadsheet
   *
   * @param file a File object.
   * @return a Process object.
   *
   * @throws IOException if any.
   */
  @JvmStatic
  fun openSpreadsheet(file: File): Process {
    return openFile(spreadsheetBins, file)
  }

  private fun openFile(possibleBins: Iterable<String>, file: File): Process {
    for (possibleBin in possibleBins) {
      if (File(possibleBin).exists()) {
        return Runtime.getRuntime().exec(arrayOf(possibleBin, file.absolutePath))
      }
    }
    error("No Office installation found...")
  }
}
