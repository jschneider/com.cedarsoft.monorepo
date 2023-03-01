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

import it.neckar.open.Sleep.forever
import it.neckar.open.Sleep.now
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 *
 */
class SleepTest {
  @Disabled
  @Test
  fun testIt() {
    Assertions.assertTimeout(Duration.ofMillis(210)) {
      val start = System.currentTimeMillis()
      now(100)
      val time = System.currentTimeMillis() - start
      Assert.assertTrue(time.toString(), time > 99)
      Assert.assertTrue(time.toString(), time < 105)
    }
  }

  @Test
  fun testInterr() {
    val called = booleanArrayOf(false)
    val thread = Thread {
      try {
        forever()
        Assert.fail("Where is the Exception")
      } catch (ignore: InterruptedException) {
        called[0] = true
      }
    }
    thread.start()
    Assert.assertFalse(called[0])
    thread.interrupt()
    thread.join()
    Assert.assertTrue(called[0])
  }
}
