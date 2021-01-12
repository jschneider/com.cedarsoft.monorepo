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

import com.google.inject.Binder
import com.google.inject.Key
import com.google.inject.util.Types
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * GuiceHelper class.
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object GuiceHelper {
  /**
   *
   * superCollectionOf
   *
   * @param type a Type object.
   * @return a ParameterizedType object.
   */
  @JvmStatic
  fun superCollectionOf(type: Type): ParameterizedType {
    return Types.newParameterizedType(MutableCollection::class.java, Types.subtypeOf(type))
  }

  /**
   *
   * superListOf
   *
   * @param type a Type object.
   * @return a ParameterizedType object.
   */
  @JvmStatic
  fun superListOf(type: Type): ParameterizedType {
    return Types.newParameterizedType(MutableList::class.java, Types.subtypeOf(type))
  }

  /**
   * Binds a wildcard collection to the set of that type
   *
   * @param binder the binder
   * @param type   the type
   * @param <T>    a T object.
   */
  @JvmStatic
  fun <T> bindWildcardCollectionForSet(binder: Binder, type: Type) {
    val key = Key.get(superCollectionOf(type)) as Key<Collection<T?>>
    val targetKey = Key.get(Types.setOf(type)) as Key<out Collection<T?>>
    binder.bind(key).to(targetKey)
  }
}
