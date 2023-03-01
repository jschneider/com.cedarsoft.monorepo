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
package it.neckar.open

import it.neckar.open.Strings.cut
import it.neckar.open.Strings.cutNull
import it.neckar.open.Strings.stripQuotes
import org.junit.Assert
import org.junit.jupiter.api.Test

/**
 *
 *
 * Date: May 29, 2007<br></br>
 * Time: 2:12:13 PM<br></br>
 */
class StringsTest {
  @Test
  fun testCut() {
    Assert.assertEquals("asd", cut("asdf", 3))
    Assert.assertEquals("asdf", cut("asdf", 4))
    Assert.assertEquals("asdf", cut("asdf", 45))
    Assert.assertEquals("", cut("asdf", 0))
    Assert.assertNull(cutNull(null, 3))
  }

  @Test
  fun testMaxLengt() {
    Assert.assertEquals("", cut("asdf", 0))
    Assert.assertEquals("a", cut("asdf", 1))
    Assert.assertEquals("as", cut("asdf", 2))
    Assert.assertEquals("asd", cut("asdf", 3))
    Assert.assertEquals("asdf", cut("asdf", 4))
    Assert.assertEquals("asdf", cut("asdf", 5))
    Assert.assertEquals("asdf", cut("asdf", 995))
  }

  @Test
  fun testQuote() {
    Assert.assertEquals("", stripQuotes(""))
    Assert.assertEquals("a", stripQuotes("a"))
    Assert.assertEquals("a", stripQuotes("\"a"))
    Assert.assertEquals("a", stripQuotes("\"a\""))
    Assert.assertEquals("a", stripQuotes("a\""))
  }
}
