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

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Customer {
  @Nonnull
  private final StringProperty name = new SimpleStringProperty(this, "name", "");

  @Nonnull
  private final ObservableList<Address> addresses = FXCollections.observableArrayList();

  public Customer() {
    ObservableBooleanValue addressesEmpty = new ReadOnlyBooleanWrapper();

    StringBinding stringEmpty = new When(Bindings.isEmpty(addresses)).then("No addresses").otherwise("dada");
  }

  @Nonnull
  public String getName() {
    return name.get();
  }

  @Nonnull
  public StringProperty nameProperty() {
    return name;
  }

  public void setName(@Nonnull String name) {
    this.name.set(name);
  }

  @Nonnull
  public ObservableList<Address> addressesProperty() {
    return addresses;
  }

  @Nonnull
  public List<Address> getAddresses() {
    return Collections.unmodifiableList(addresses);
  }

  public static class Address {

    @Nonnull
    private final StringProperty street = new SimpleStringProperty(this, "street", "");
    @Nonnull
    private final IntegerProperty number = new SimpleIntegerProperty(this, "number", 1);
    @Nonnull
    private final StringProperty city = new SimpleStringProperty(this, "city", "");

    @Nonnull
    private final ReadOnlyBooleanWrapper valid = new ReadOnlyBooleanWrapper();

    {
      valid.bind(number
                   .greaterThan(0)
                   .and(street.isNotEmpty())
                   .and(city.isNotEmpty()));

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
    }

    @Nonnull
    public String getStreet() {
      return street.get();
    }

    @Nonnull
    public StringProperty streetProperty() {
      return street;
    }

    public void setStreet(@Nonnull String street) {
      this.street.set(street);
    }

    public int getNumber() {
      return number.get();
    }

    @Nonnull
    public IntegerProperty numberProperty() {
      return number;
    }

    public void setNumber(int number) {
      this.number.set(number);
    }

    @Nonnull
    public String getCity() {
      return city.get();
    }

    @Nonnull
    public StringProperty cityProperty() {
      return city;
    }

    public void setCity(@Nonnull String city) {
      this.city.set(city);
    }

    public boolean isValid() {
      return valid.get();
    }

    public ReadOnlyBooleanProperty validProperty() {
      return valid.getReadOnlyProperty();
    }
  }
}
