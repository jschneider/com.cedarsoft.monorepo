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

package it.neckar.open.test.io2;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;

import javax.annotation.Nonnull;

import it.neckar.open.serialization.StreamSerializer;
import it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2;
import it.neckar.open.serialization.test.utils.Entry;

import it.neckar.open.test.Car;
import it.neckar.open.test.Extra;
import it.neckar.open.test.Model;
import it.neckar.open.test.Money;
import it.neckar.open.test.io.ModelSerializer;

/**
 *
 */
public class CarSerializerTest extends AbstractXmlSerializerTest2<Car> {
  @Nonnull
  @Override
  protected StreamSerializer<Car> getSerializer() throws Exception {
    MoneySerializer moneySerializer = new MoneySerializer();
    return new CarSerializer( moneySerializer, new ExtraSerializer( moneySerializer ), new ModelSerializer() );
  }

  public static final Entry<? extends Car> ENTRY1 = create(
    new Car(new Model("Ford"), Color.ORANGE, new Money(19000, 00), Arrays.asList(new Extra("Whoo effect", new Money(99, 98)), new Extra("Better Whoo effect", new Money(199, 00))))
    , "<car>\n" +
        "  <color red=\"255\" blue=\"0\" green=\"200\" />\n" +
        "  <model>Ford</model>\n" +
        "  <basePrice cents=\"1900000\" />\n" +
        "  <extra>\n" +
        "    <description>Whoo effect</description>\n" +
        "    <price cents=\"9998\" />\n" +
        "  </extra>\n" +
        "  <extra>\n" +
        "    <description>Better Whoo effect</description>\n" +
        "    <price cents=\"19900\" />\n" +
        "  </extra>\n" +
        "</car>");

  @Override
  protected void verifyDeserialized(@Nonnull Car deserialized, @Nonnull Car original) {
    assertEquals( Color.ORANGE, deserialized.getColor() );
    assertEquals( deserialized.getBasePrice(), new Money( 19000, 0 ) );
    //.... (and much more)
  }
}
