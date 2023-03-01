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
package it.neckar.open.test

import it.neckar.open.javafx.properties.*
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.beans.binding.When
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.util.Collections

/**
 */
class Customer {
  val nameProperty: StringProperty = SimpleStringProperty(this, "name", "")
  var name: String by nameProperty

  val addresses: ObservableList<Address> = FXCollections.observableArrayList<Address>()

  val addressesEmpty: ObservableBooleanValue = Bindings.isEmpty(addresses)
  val stringEmpty: StringBinding = When(addressesEmpty).then("No addresses").otherwise("dada")

  fun getAddresses(): List<Address> {
    return Collections.unmodifiableList(addresses)
  }

  class Address {
    val streetProperty: StringProperty = SimpleStringProperty(this, "street", "")
    var street: String by streetProperty

    val numberProperty: IntegerProperty = SimpleIntegerProperty(this, "number", 1)
    var number: Int by numberProperty

    val cityProperty: StringProperty = SimpleStringProperty(this, "city", "")
    var city: String by cityProperty

    val validProperty: ReadOnlyBooleanProperty = SimpleBooleanProperty().also {
      it.bind(
        numberProperty
          .greaterThan(0)
          .and(streetProperty.isNotEmpty)
          .and(cityProperty.isNotEmpty)
      )
    }
    val valid: Boolean by validProperty

    //init {
    //InvalidationListener validationUpdateListener = new InvalidationListener() {
    //  @Override
    //  public void invalidated(Observable observable) {
    //    updateValidation();
    //  }
    //
    //  private void updateValidation() {
    //    valid.set(!street.getValue().isEmpty() && !city.getValue().isEmpty() && number.get() > 0);
    //  }
    //};
    //street.addListener(validationUpdateListener);
    //number.addListener(validationUpdateListener);
    //city.addListener(validationUpdateListener);
    //}
  }
}
