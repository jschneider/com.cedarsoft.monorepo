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
package com.cedarsoft.commons.test

import javafx.beans.InvalidationListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableListBase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Arrays

/**
 */
class CustomerTest {
  private lateinit var customer: Customer

  @BeforeEach
  fun setUp() {
    customer = Customer()
  }

  @Test
  fun testLazyObsList() {
    object : ObservableListBase<String>() {

      override val size: Int
        get() {
          return 10000
        }

      override fun get(index: Int): String {
        return index.toString() + ""
      }
    }
  }

  @Test
  fun testSimpleBinding() {
    val address = Customer.Address()
  }

  @Test
  fun testBasics() {
    Assertions.assertThat(customer.name).isEqualTo("")
  }

  @Test
  fun testListeners() {
    customer.nameProperty.addListener(InvalidationListener { println("Invalidated") })
    customer.nameProperty.addListener { observable, oldValue, newValue -> println("Changed from <$oldValue> to <$newValue>") }
    println("setting name")
    customer.name = "New Name"
    println("name set")
  }

  @Test
  fun testAddressValid() {
    val address = Customer.Address()
    Assertions.assertThat(address.valid).isFalse
    address.validProperty.addListener { observable, oldValue, newValue -> println("Changed from <$oldValue> to <$newValue>") }
    address.city = "daCity"
    address.street = "daStreet"
    println("setting number")
    address.number = 2
  }

  @Test
  fun testList() {
    customer.addresses.addListener(ListChangeListener { c ->
      println("Change... $c")
      while (c.next()) {
        println("next <" + c.addedSubList + "> and removed <" + c.removed + ">")
      }
    })
    println("add")
    customer.addresses.add(Customer.Address())
    println("add")
    customer.addresses.add(Customer.Address())
    println("addAll")
    customer.addresses.addAll(Arrays.asList(Customer.Address(), Customer.Address(), Customer.Address()))
  }
}
