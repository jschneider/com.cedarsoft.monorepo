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

import com.google.inject.Guice
import com.google.inject.Key
import com.google.inject.Module
import java.util.ArrayList
import java.util.Collections
import javax.annotation.Nonnull

/**
 *
 * GuiceModulesHelper class.
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object GuiceModulesHelper {
  /**
   *
   * minimize
   *
   * @param modules   a List object.
   * @param testTypes a Class object.
   * @return a GuiceModulesHelper.Result object.
   */
  @JvmStatic
  fun minimize(modules: List<Module>, vararg testTypes: Class<*>): Result {
    return minimize(modules, *convertToKeys(*testTypes))
  }

  /**
   * Converts the cs array to a keys array
   */
  @JvmStatic
  fun convertToKeys(vararg types: Class<*>): Array<Key<*>> {
    return types.map {
      Key.get(it)
    }.toTypedArray()
  }

  /**
   *
   * minimize
   *
   * @param modules a List object.
   * @param keys    a Key object.
   * @return a GuiceModulesHelper.Result object.
   */
  @JvmStatic
  fun minimize(modules: List<Module>, vararg keys: Key<*>): Result {
    //Verify to ensure it works with all modules
    verifyInjection(modules, *keys)
    return minimize(Result(modules), *keys)
  }

  /**
   *
   * minimize
   *
   * @param result   a GuiceModulesHelper.Result object.
   * @param testType a Class object.
   * @return a GuiceModulesHelper.Result object.
   */
  @JvmStatic
  fun minimize(result: Result, testType: Class<*>): Result {
    return minimize(result, Key.get(testType))
  }

  /**
   *
   * minimize
   *
   * @param result a GuiceModulesHelper.Result object.
   * @param keys   a Key object.
   * @return a GuiceModulesHelper.Result object.
   */
  @JvmStatic
  fun minimize(result: Result, vararg keys: Key<*>): Result {
    //Iterate over all types (copy because the result is updated)
    val modules: List<Module> = ArrayList(result.getTypes())
    for (current in modules) {
      try {
        val copy: MutableCollection<Module> = ArrayList(modules)
        copy.remove(current)
        verifyInjection(copy, *keys)

        //Update the result
        result.remove(current)
        if (copy.isEmpty()) {
          result.removeAll()
          return result //fast exit
        }

        //Try to minimize further
        return minimize(result, *keys)
      } catch (ignore: Exception) {
      }
    }
    return result //no minimization
  }

  private fun verifyInjection(modules: Iterable<Module>, vararg keys: Key<*>) {
    require(keys.isNotEmpty()) { "Need at least one key" }
    val injector = Guice.createInjector(modules)

    keys.forEach {
      injector.getInstance(it)
    }
  }

  /**
   *
   * assertMinimizeNotPossible
   *
   * @param modules   a List object.
   * @param testTypes the Class types.
   * @throws AssertionError if any.
   */
  @JvmStatic
  @Throws(AssertionError::class)
  fun assertMinimizeNotPossible(modules: List<Module>, vararg testTypes: Class<*>) {
    assertMinimizeNotPossible(modules, *convertToKeys(*testTypes))
  }

  /**
   *
   * assertMinimizeNotPossible
   *
   * @param modules a List object.
   * @param keys    a Key object.
   * @throws AssertionError if any.
   */
  @JvmStatic
  @Throws(AssertionError::class)
  fun assertMinimizeNotPossible(modules: List<Module>, vararg keys: Key<*>) {
    val minimal = minimize(modules, *keys)
    if (minimal.getRemoved().isNotEmpty()) {
      throw AssertionError(
        """
  Can be minimized:
  Remove:
  ${minimal.removedClassNamesAsString}${minimal.asInstantiations()}
  """.trimIndent()
      )
    }
  }

  class Result(types: List<Module>) {
    private val types: MutableList<out Module>

    private val removed: MutableList<Module> = ArrayList()
    fun size(): Int {
      return types.size
    }

    fun removeAll() {
      removed.addAll(types)
      types.clear()
    }

    fun remove(@Nonnull toRemove: Module) {
      types.remove(toRemove)
      removed.add(toRemove)
    }

    fun getTypes(): List<Module> {
      return Collections.unmodifiableList(types)
    }

    fun getRemoved(): List<Module> {
      return Collections.unmodifiableList(removed)
    }

    val removedClassNamesAsString: String
      get() {
        return buildString {
          removed.forEach {
            append("- ")
            append(it.javaClass.name)
            append("\n")
          }
        }
      }

    fun asInstantiations(): String {
      val builder = StringBuilder()
      val iterator: Iterator<Module> = types.iterator()
      while (iterator.hasNext()) {
        val module = iterator.next()
        builder.append("new ").append(module.javaClass.name).append("()")
        if (iterator.hasNext()) {
          builder.append(", ")
        }
      }
      return builder.toString()
    }

    init {
      this.types = ArrayList(types)
    }
  }
}
