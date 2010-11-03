/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cedarsoft;

import com.cedarsoft.unit.other.dpi;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.cm;
import com.cedarsoft.unit.si.m;
import com.cedarsoft.unit.si.m2;
import com.cedarsoft.unit.si.mm;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UsageSample {
  @Test
  public void testArea1() {
    @m
    double length = 7.3;
    @m
    double width = 12;
    @m2
    double area = calculateArea( length, width );

    assertEquals( 87.6, area, 0 );
  }

  @Test
  public void testArea2() {
    @m
    double length = 7.3;
    @cm
    double width = 1200;
    @m2
    double area = calculateAreaSpecial( length, width );

    assertEquals( 87.6, area, 0 );
  }

  @Test
  public void testPxToMm() throws Exception {
    @dpi
    int res = 72;
    assertEquals( 10, pixelsToMm( 28, res ), 0.2 );
  }

  /**
   * This is a simple method that takes two length arguments of a given unit
   *
   * @param length the length
   * @param width  the width
   * @return the area
   */
  @m2
  public static double calculateArea( @m double length, @m double width ) {
    return length * width;
  }

  /**
   * This method converts one unit. The conversion has to be done manually.
   * But this is a strength! Not every unit can be converted losless. Therefore doing it manually if
   * necessary seems to be the better idea.
   *
   * @param length the length
   * @param width  the width
   * @return the area
   */
  @m2
  public static double calculateAreaSpecial( @m double length, @cm double width ) {
    return length * width / 100.0;
  }

  @mm
  public static double pixelsToMm( @px int pixels, @dpi double resolution ) {
    return pixels / resolution * 25.4;
  }
}
