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
package it.neckar.open.inject

import it.neckar.open.inject.GuiceModulesHelper.assertMinimizeNotPossible
import it.neckar.open.inject.GuiceModulesHelper.minimize
import com.google.inject.AbstractModule
import com.google.inject.ConfigurationException
import com.google.inject.Key
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Arrays

/**
 *
 */
class GuiceModulesHelperTest {
  @Test
  fun testFail() {
    try {
      minimize(Arrays.asList(Module1(), Module2()), MutableList::class.java)
      Assertions.fail<Any>("Where is the Exception")
    } catch (ignore: ConfigurationException) {
    }
  }

  @Test
  fun testMinimization() {
    val result = minimize(Arrays.asList(Module1(), Module2()), Any::class.java)
    assertEquals(2, result.getRemoved().size)
    assertEquals(0, result.getTypes().size)
  }

  @Test
  fun testMinimization2() {
    val result = minimize(Arrays.asList(Module1(), Module2()), MyObject::class.java)
    assertEquals(1, result.getRemoved().size)
    assertEquals(1, result.getTypes().size)
  }

  @Test
  fun testMinimization3() {
    val result = GuiceModulesHelper.Result(Arrays.asList(Module1(), Module2()))
    Assertions.assertSame(result, minimize(result, MyObject::class.java))
    assertEquals(1, result.getRemoved().size)
    assertEquals(1, result.getTypes().size)
  }

  @Test
  @Throws(Exception::class)
  fun testAssertMini() {
    assertThrows(AssertionError::class.java) { assertMinimizeNotPossible(Arrays.asList(Module1(), Module2()), MyObject::class.java) }
  }

  @Test
  @Throws(Exception::class)
  fun testAssertMini4() {
    assertThrows(AssertionError::class.java) { assertMinimizeNotPossible(listOf(Module2()), Any::class.java) }
  }

  @Test
  @Throws(Exception::class)
  fun testAssertMini2() {
    assertMinimizeNotPossible(listOf(Module1()), MyObject::class.java, Any::class.java)
  }

  @Test
  @Throws(Exception::class)
  fun testAssertMini3() {
    //    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), MyObject.class, Object.class );
    assertMinimizeNotPossible(listOf(Module1()), MyObject::class.java)
    assertMinimizeNotPossible(listOf(Module1()), Any::class.java, MyObject::class.java)
    assertMinimizeNotPossible(listOf(Module1()), object : Key<MyObject?>() {}, object : Key<Any?>() {})
    assertMinimizeNotPossible(listOf(Module1()), object : Key<Any?>() {}, object : Key<MyObject?>() {}, object : Key<Any?>() {})
  }

  class Module1 : AbstractModule() {
    override fun configure() {
      bind(MyObject::class.java).toInstance(MyObject("theId"))
    }
  }

  class Module2 : AbstractModule() {
    override fun configure() {}
  }

  class MyObject(private val id: String)
}
