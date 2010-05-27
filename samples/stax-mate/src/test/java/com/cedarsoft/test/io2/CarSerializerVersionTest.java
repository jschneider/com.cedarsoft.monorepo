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

package com.cedarsoft.test.io2;

import com.cedarsoft.Version;
import com.cedarsoft.serialization.AbstractSerializer;
import com.cedarsoft.serialization.AbstractXmlVersionTest;
import com.cedarsoft.serialization.Serializer;
import com.cedarsoft.serialization.ui.DelegatesMappingVisualizer;
import com.cedarsoft.test.Car;
import com.cedarsoft.test.Money;
import com.cedarsoft.test.io.ModelSerializer;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

/**
 *
 */
public class CarSerializerVersionTest extends AbstractXmlVersionTest<Car> {
  @NotNull
  @Override
  protected Serializer<Car> getSerializer() throws Exception {
    MoneySerializer moneySerializer = new MoneySerializer();
    return new CarSerializer( moneySerializer, new ExtraSerializer( moneySerializer ), new ModelSerializer() );
  }

  @NotNull
  @Override
  protected Map<? extends Version, ? extends String> getSerializedXml() {
    Map<Version, String> map = new HashMap<Version, String>();

    map.put( Version.valueOf( 1, 0, 0 ), "<car>\n" +
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
    map.put( Version.valueOf( 1, 0, 1 ), "<car>\n" +
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
      "</car>" );

    return map;
  }

  @Override
  protected void verifyDeserialized( @NotNull Car deserialized, @NotNull Version version ) throws Exception {
    assertEquals( deserialized.getColor(), Color.ORANGE );
    assertEquals( deserialized.getBasePrice(), new Money( 19000, 0 ) );
    //.... (and much more)
  }

  @Test
  public void testAsciiArt() throws Exception {
    DelegatesMappingVisualizer visualizer = new DelegatesMappingVisualizer( ( ( AbstractSerializer<?, ?, ?, ?> ) getSerializer() ).getDelegatesMappings() );
    assertEquals( visualizer.visualize(),
                  "         -->     Extra     Model     Money\n" +
                    "------------------------------------------\n" +
                    "   1.0.0 -->     1.5.0     1.0.0     1.0.0\n" +
                    "   1.0.1 -->     1.5.1       |       1.0.1\n" +
                    "------------------------------------------\n" );
  }

}
