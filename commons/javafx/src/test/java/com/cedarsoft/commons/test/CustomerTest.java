/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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
package com.cedarsoft.commons.test;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableListBase;
import org.junit.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CustomerTest {

  private Customer customer;

  @Before
  public void setUp() throws Exception {
    customer = new Customer();
  }

  @Test
  public void testLazyObsList() throws Exception {
    new ObservableListBase<String>() {
      @Override
      public int size() {
        return 10000;
      }

      @Override
      public String get(int index) {
        return index + "";
      }
    };
  }

  @Test
  public void testSimpleBinding() throws Exception {
    Customer.Address address = new Customer.Address();
  }

  @Test
  public void testBasics() throws Exception {
    assertThat(customer.getName()).isEqualTo("");
  }

  @Test
  public void testListeners() throws Exception {
    customer.nameProperty().addListener(new InvalidationListener() {
      @Override
      public void invalidated(Observable observable) {
        System.out.println("Invalidated");
      }
    });

    customer.nameProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("Changed from <" + oldValue + "> to <" + newValue + ">");
      }
    });

    System.out.println("setting name");
    customer.setName("New Name");
    System.out.println("name set");
  }

  @Test
  public void testAddressValid() throws Exception {
    Customer.Address address = new Customer.Address();
    assertThat(address.isValid()).isFalse();

    address.validProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        System.out.println("Changed from <" + oldValue + "> to <" + newValue + ">");
      }
    });

    address.setCity("daCity");
    address.setStreet("daStreet");
    System.out.println("setting number");
    address.setNumber(2);
  }

  @Test
  public void testList() throws Exception {
    customer.addressesProperty().addListener(new ListChangeListener<Customer.Address>() {
      @Override
      public void onChanged(Change<? extends Customer.Address> c) {
        System.out.println("Change... " + c);
        while (c.next()) {
          System.out.println("next <" + c.getAddedSubList() + "> and removed <" + c.getRemoved() + ">");
        }
      }
    });


    System.out.println("add");
    customer.addressesProperty().add(new Customer.Address());
    System.out.println("add");
    customer.addressesProperty().add(new Customer.Address());
    System.out.println("addAll");
    customer.addressesProperty().addAll(Arrays.asList(new Customer.Address(), new Customer.Address(), new Customer.Address()));
  }
}