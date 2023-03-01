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
package it.neckar.open.serialization.ui

import it.neckar.open.serialization.ToString
import it.neckar.open.serialization.VersionMappings
import it.neckar.open.version.Version
import java.io.IOException
import java.io.StringWriter
import java.io.Writer
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

/**
 * @param <T> the type
</T> */
open class VersionMappingsVisualizer<T : Any> @JvmOverloads constructor(
  private val mappings: VersionMappings<T>,
  private val comparator: Comparator<T> = ToStringComparator(),
  private val toString: (T) -> String = { it.toString() }
) {

  constructor(mappings: VersionMappings<T>, toString: (T) -> String = { it.toString() }) : this(mappings, ToStringComparator<T>(), toString)


  @Throws(IOException::class)
  fun visualize(): String {
    val writer = StringWriter()
    visualize(writer)
    return writer.toString()
  }

  @Throws(IOException::class)
  fun visualize(out: Writer) {
    val columns: MutableCollection<Column> = ArrayList()

    //The versions
    val keyVersions = mappings.mappedVersions

    //The keys
    val keys: List<T> = ArrayList(mappings.getMappings().keys)
    Collections.sort(keys, comparator)
    for (key in keys) {
      val mapping = mappings.getMapping(key)
      val versions: MutableList<Version> = ArrayList()
      for (keyVersion in keyVersions) {
        versions.add(mapping.resolveVersion(keyVersion))
      }
      columns.add(Column(toString(key), versions))
    }
    writeHeadline(columns, out)
    writeSeparator(columns.size, out)
    writeContent(ArrayList(keyVersions), columns, out)
    writeSeparator(columns.size, out)
  }

  @Throws(IOException::class)
  protected fun writeHeadline(columns: Iterable<Column>, out: Writer) {
    out.write(extend("")) //first column
    out.write(FIRST_COLUMN_SEPARATOR)
    for (column in columns) {
      out.write(COL_SEPARATOR) //delimiter
      out.write(extend(column.header))
    }
    out.write("\n")
  }

  class Column(val header: String, versions: Iterable<Version>) {

    val lines: MutableList<String> = ArrayList()

    init {
      var lastVersion: Version? = null
      for (version in versions) {
        if (version == lastVersion) {
          lines.add(COL_VERSION_REPEAT)
        } else {
          lines.add(version.format())
        }
        lastVersion = version
      }
    }
  }

  class ToStringComparator<T> : Comparator<T> {
    override fun compare(o1: T, o2: T): Int {
      return o1.toString().compareTo(o2.toString())
    }
  }

  companion object {
    private const val COL_SEPARATOR = "  "

    private const val FIRST_COLUMN_SEPARATOR = " -->"
    private const val COL_WIDTH = 8

    private const val COL_VERSION_REPEAT = "  |"

    @Throws(IOException::class)
    private fun writeContent(keyVersions: List<Version>, columns: Iterable<Column>, out: Writer) {
      var i = 0
      val keyVersionsSize = keyVersions.size
      while (i < keyVersionsSize) {
        val keyVersion = keyVersions[i]
        out.write(extend(keyVersion.format()))
        out.write(FIRST_COLUMN_SEPARATOR)


        //Now write the columns
        for (column in columns) {
          out.write(COL_SEPARATOR)
          out.write(extend(column.lines[i]))
        }
        out.write("\n")
        i++
      }
    }

    @Throws(IOException::class)
    private fun writeSeparator(columnsSize: Int, out: Writer) {
      var count = COL_WIDTH
      count += FIRST_COLUMN_SEPARATOR.length
      count += COL_SEPARATOR.length * columnsSize
      count += COL_WIDTH * columnsSize
      val builder = StringBuilder()
      for (i in 0 until count) {
        builder.append("-")
      }
      out.write(builder.append("\n").toString())
    }

    @JvmStatic
    fun <T : Any> create(mappings: VersionMappings<T>, comparator: Comparator<T>, toString: (T) -> String = { it.toString() }): VersionMappingsVisualizer<T> {
      return VersionMappingsVisualizer(mappings, comparator, toString)
    }

    @JvmStatic
    fun <T : Any> create(mappings: VersionMappings<T>, comparator: Comparator<T>, toString: ToString<T>): VersionMappingsVisualizer<T> {
      return VersionMappingsVisualizer(mappings, comparator, toString::convert)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T : Any> toString(mappings: VersionMappings<T>, toString: (T) -> String = { it.toString() }): String {
      return VersionMappingsVisualizer(mappings, toString).visualize()
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T : Any> toString(mappings: VersionMappings<T>, toString: ToString<T>): String {
      return VersionMappingsVisualizer(mappings, toString::convert).visualize()
    }

    private fun extend(string: String): String {
      if (string.length > COL_WIDTH) {
        return string.substring(0, COL_WIDTH)
      }
      val builder = StringBuilder()
      for (i in 0 until COL_WIDTH - string.length) {
        builder.append(" ")
      }
      return builder.append(string).toString()
    }
  }
}
