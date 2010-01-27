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

package com.cedarsoft.test.io;

import com.cedarsoft.serialization.AbstractXmlSerializerMultiTest;
import com.cedarsoft.serialization.Serializer;
import com.cedarsoft.test.Car;
import com.cedarsoft.test.Extra;
import com.cedarsoft.test.Model;
import com.cedarsoft.test.Money;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

/**
 *
 */
public class CarSerializerTest extends AbstractXmlSerializerMultiTest<Car> {
  @NotNull
  @Override
  protected Serializer<Car> getSerializer() {
    MoneySerializer moneySerializer = new MoneySerializer();
    //We can share the same serializer. But we don't have to.
    return new CarSerializer( moneySerializer, new ExtraSerializer( moneySerializer ), new ModelSerializer() );
  }

  @NotNull
  @Override
  protected Iterable<? extends Car> createObjectsToSerialize() {
    return Arrays.asList(
      new Car( new Model( "Toyota" ), Color.BLACK, new Money( 49000, 00 ) ),
      new Car( new Model( "Ford" ), Color.ORANGE, new Money( 19000, 00 ), Arrays.asList( new Extra( "Whoo effect", new Money( 99, 98 ) ), new Extra( "Better Whoo effect", new Money( 199, 00 ) ) ) )
    );
  }

  @NotNull
  @Override
  protected List<? extends String> getExpectedSerialized() {
    return Arrays.asList(
      "<car>\n" +
        "  <color red=\"0\" blue=\"0\" green=\"0\" />\n" +
        "  <model>Toyota</model>\n" +
        "  <basePrice>4900000</basePrice>\n" +
        "</car>",
      "<car>\n" +
        "  <color red=\"255\" blue=\"0\" green=\"200\" />\n" +
        "  <model>Ford</model>\n" +
        "  <basePrice>1900000</basePrice>\n" +
        "  <extra>\n" +
        "    <description>Whoo effect</description>\n" +
        "    <price>9998</price>\n" +
        "  </extra>" +
        " <extra>\n" +
        "    <description>Better Whoo effect</description>\n" +
        "    <price>19900</price>\n" +
        "  </extra>" +
        "</car>" );
  }

  @Override
  protected void verifyDeserialized( @NotNull List<? extends Car> deserialized ) {
    //We don't implement equals in the car, therefore compare manually
    //    super.verifyDeserialized( deserialized );

    assertEquals( deserialized.size(), 2 );

    Car first = deserialized.get( 0 );
    assertEquals( first.getColor(), Color.BLACK );
    assertEquals( first.getBasePrice(), new Money( 49000, 0 ) );

    //....

  }
}
