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
package it.neckar.open.io

import it.neckar.open.test.utils.TemporaryFolder
import it.neckar.open.test.utils.WithTempFiles
import org.apache.commons.lang3.SystemUtils
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import javax.annotation.Nonnull

/**
 *
 */
@WithTempFiles
class LinkUtilsTest {
  @Test
  fun testLinkTypes() {
    assertNotNull(LinkType.HARD)
    assertNotNull(LinkType.SYMBOLIC)
  }

  @Test
  @Throws(Exception::class)
  fun testCreation(@Nonnull tmp: TemporaryFolder) {
    if (SystemUtils.IS_OS_WINDOWS) {
      return
    }
    val target = File.createTempFile("asdf", "linked.to")
    target.createNewFile()
    assertFalse(LinkUtils.isLink(target))
    val dir = tmp.newFolder()
    val link = File(dir, "link")
    assertFalse(link.exists())

    LinkUtils.createSymbolicLink(target, link)
    assertTrue(link.exists())
    assertTrue(LinkUtils.isLink(link))
    assertTrue(target.exists())

    link.delete()
    assertTrue(target.exists())
    assertFalse(link.exists())
  }
}
