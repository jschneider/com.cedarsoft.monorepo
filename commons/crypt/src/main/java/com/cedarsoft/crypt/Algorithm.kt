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

package com.cedarsoft.crypt


import com.google.common.collect.ImmutableList
import java.security.MessageDigest
import java.util.Collections

/**
 * Represents an algorithm
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
enum class Algorithm
/**
 * Creates a new algorithm.
 * The first alternative name will be used to get the MessageDigest
 */
constructor(val expectedLength: Int, vararg alternativeNames: String) {
  MD5(128, "MD5"),
  SHA1(128, "SHA-1", "SHA1"),
  SHA256(256, "SHA-256", "SHA256"),
  SHA512(512, "SHA-512", "SHA512");

  private val alternativeNames: ImmutableList<String> = ImmutableList.copyOf(alternativeNames)

  /**
   * Creates a new message digest for the algorithm
   */
  val messageDigest: MessageDigest
    get() {
      return MessageDigest.getInstance(alternativeNames[0])
    }

  /**
   * Returns a list containing all alternative names for the given algorithm
   *
   * @return the alternative names for the algorithm
   */
  fun getAlternativeNames(): List<String> {
    return Collections.unmodifiableList(alternativeNames)
  }

  companion object {
    @JvmStatic
    fun getAlgorithm(algorithmString: String): Algorithm {
      //First search for the exact match
      try {
        return Algorithm.valueOf(algorithmString)
      } catch (ignore: IllegalArgumentException) {
      }

      //Now search for the alternative names
      for (algorithm in Algorithm.values()) {
        if (algorithm.alternativeNames.contains(algorithmString)) {
          return algorithm
        }
      }
      throw IllegalArgumentException("No algorithm found for $algorithmString")
    }
  }
}
