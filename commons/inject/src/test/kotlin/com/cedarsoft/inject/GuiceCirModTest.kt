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
package com.cedarsoft.inject

import com.cedarsoft.inject.GuiceModulesHelper.minimize
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Provides
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Arrays
import javax.inject.Inject
import javax.inject.Qualifier

/**
 *
 */
class GuiceCirModTest {
  @Test
  fun testIt() {
    val myObject = Guice.createInjector(Module1(), Module2()).getInstance(MyObject::class.java)
    Assertions.assertEquals("", Guice.createInjector(Module1(), Module2()).getInstance(String::class.java))
    Assertions.assertEquals(7, myObject.id.toLong())
    val result = minimize(Arrays.asList(Module1(), Module2()), MyObject::class.java)
    Assertions.assertEquals(0, result.getRemoved().size.toLong())
  }

  class Module1 : AbstractModule() {
    override fun configure() {}

    @Provides
    fun providesInteger(@StringMarker string: String?): Int {
      Assertions.assertEquals("magic", string)
      return 7
    }
  }

  class Module2 : AbstractModule() {
    override fun configure() {
      bind(String::class.java).annotatedWith(StringMarker::class.java).toInstance("magic")
    }
  }

  class MyObject @Inject constructor(val id: Int)

  @Retention(AnnotationRetention.RUNTIME)
  @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
  @MustBeDocumented
  @Qualifier
  annotation class StringMarker
}
