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
package com.cedarsoft.version

import com.cedarsoft.version.Version.Companion.parse
import com.google.common.base.Splitter
import org.junit.Assert
import java.util.concurrent.Callable
import javax.annotation.Nonnull

/**
 *
 */
object VersionParsingPerformance {
  @Nonnull
  val DOT_SPLITTER: Splitter = Splitter.on(".")

  @Nonnull
  val VERSION: String = "1.2.3-suffix"

  @JvmStatic
  fun main(args: Array<String>) {
    run("String.plit") {
      val parts = VERSION.split("\\.").dropLastWhile { it.isEmpty() }.toTypedArray()
      require(parts.size == 3) { "Version <$VERSION> must contain exactly three parts delimited with '.'" }
      val build: Int
      val suffix: String?
      if (parts[2].contains("-")) {
        val firstIndex = parts[2].indexOf('-')
        val buildAsString = parts[2].substring(0, firstIndex)
        build = buildAsString.toInt()
        suffix = parts[2].substring(firstIndex + 1, parts[2].length)
      } else {
        build = parts[2].toInt()
        suffix = null
      }
      val major = parts[0].toInt()
      val minor = parts[1].toInt()
      Version(major, minor, build, suffix)
    }
    run("static Splitter") {
      val parts = DOT_SPLITTER.split(VERSION)
      val iterator: Iterator<String> = parts.iterator()
      val part0 = iterator.next()
      val part1 = iterator.next()
      val part2 = iterator.next()
      require(!iterator.hasNext()) { "In" }
      val index = part2.indexOf("-")
      if (index > -1) {
        val suffix = part2.substring(index + 1)
        val build = part2.substring(0, index).toInt()
        Version(part0.toInt(), part1.toInt(), build, suffix)
      } else {
        Version(part0.toInt(), part1.toInt(), part0.toInt())
      }
    }
    run("version.parse") { parse(VERSION) }
    run("indexOf") {
      val index0 = VERSION.indexOf(".")
      val index1 = VERSION.indexOf(".", index0 + 2)
      val indexMinus = VERSION.indexOf("-", index1 + 2)
      require(!(index0 == -1 || index1 == -1))
      val major = VERSION.substring(0, index0).toInt()
      val minor = VERSION.substring(index0 + 1, index1).toInt()
      val build = VERSION.substring(index1 + 1, indexMinus).toInt()
      val suffix = VERSION.substring(indexMinus + 1)
      Version(major, minor, build, suffix)
    }
  }

  @Throws(Exception::class)
  private fun run(@Nonnull description: String, @Nonnull callable: Callable<Version>) {
    //Warmup
    for (i in 0..9999999) {
      val (major, minor, build, suffix) = callable.call()
      Assert.assertEquals(1, major.toLong())
      Assert.assertEquals(2, minor.toLong())
      Assert.assertEquals(3, build.toLong())
      Assert.assertEquals("suffix", suffix)
    }

    //Do the work
    val start = System.currentTimeMillis()
    for (i in 0..9999999) {
      val (major, minor, build, suffix) = callable.call()
      Assert.assertEquals(1, major.toLong())
      Assert.assertEquals(2, minor.toLong())
      Assert.assertEquals(3, build.toLong())
      Assert.assertEquals("suffix", suffix)
    }
    val end = System.currentTimeMillis()
    println(description + " took " + (end - start))
  }
}
