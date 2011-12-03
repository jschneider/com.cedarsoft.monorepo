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

import com.cedarsoft.unit.quantity.Length;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Units;
import com.cedarsoft.unit.other.deg;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.prefix.Prefixed;
import com.cedarsoft.unit.prefix.centi;
import com.cedarsoft.unit.prefix.kilo;
import com.cedarsoft.unit.prefix.milli;
import com.cedarsoft.unit.si.SIBaseUnit;
import com.cedarsoft.unit.si.SiDerivedUnit;
import com.cedarsoft.unit.si.cm;
import com.cedarsoft.unit.si.kg;
import com.cedarsoft.unit.si.m;
import com.cedarsoft.unit.si.m2;
import com.cedarsoft.unit.si.m_s;
import com.cedarsoft.unit.si.mm;
import com.cedarsoft.unit.si.rad;
import com.cedarsoft.unit.si.s;
import javax.annotation.Nonnull;
import org.junit.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 *
 */
public class UnitTest {
  @m
  public static double calcMetres( @m double amount ) {
    return amount;
  }

  @m2
  public static double calcArea( @m double width, @m double height ) {
    return width * height;
  }

  @m_s
  public static double calcSpeed( @m double amount, @s double time ) {
    return amount / time;
  }

  @m
  public static double convertToMetres( @Length double amount, @Nonnull Class<? extends Annotation> unit ) {
    if ( unit == m.class ) {
      return amount;
    }

    if ( Prefixed.isPrefixed( unit ) ) {
      double factor = Prefixed.getFactor( unit );
      return amount * factor;
    }

    throw new UnsupportedOperationException( "Unsupported unit: " + unit );
  }

  @m
  public static double convertToMetres( @cm double centiMetres ) {
    return centiMetres / 100.0;
  }

  @m
  public static double large( @kilo( m.class ) double km ) {
    return km / 1000;
  }

  @milli( m.class )
  public static double cm2mm( @centi( m.class ) double cm ) {
    return cm / 10.0;
  }

  @cm
  public static double m2cm( @m double cm ) {
    return cm / 10.0;
  }

  @rad
  public static double toRadians( @deg double angdeg ) {
    return Math.toRadians( angdeg );
  }

  @deg
  public static double toDegrees( @rad double angrad ) {
    return Math.toDegrees( angrad );
  }

  ////////////////
  // The above methods are used to show the usage of the annotations
  ////////////////

  @Test
  public void testIt() {
    Class<?> metreUnit = m.class;

    {
      @m
      double metre = 7;
      @m
      double result = calcMetres( metre );
    }

    {
      @m
      double result = convertToMetres( 37, cm.class );
      assertEquals( 0.37, result, 0 );
    }

    {
      @m
      double result = convertToMetres( 37, mm.class );
      assertEquals( 0.037, result, 0 );
    }
  }

  @Test
  public void testConvert() throws Exception {
    @cm
    int cm = 100;
    @m
    double result = convertToMetres( cm );
    assertEquals( 1, result, 0 );
  }

  @Test
  public void testSpeed() throws Exception {
    @m_s
    double speed = calcSpeed( 70, 100 );
    assertEquals( 0.7, speed, 0 );
  }

  @Test
  public void testArea() throws Exception {
    @m int width = 7;
    @m int height = 3;
    @m2
    double area = calcArea( width, height );

    assertEquals( 21, area, 0 );
  }

  @Test
  public void testSymbol() throws Exception {
    assertEquals( "m", Units.getSymbol( m.class ) );
    assertEquals( "cm", Units.getSymbol( cm.class ) );
    assertEquals( "kg", Units.getSymbol( kg.class ) );
    assertEquals( "m²", Units.getSymbol( m2.class ) );
    assertEquals( "m/s", Units.getSymbol( m_s.class ) );
    assertEquals( "s", Units.getSymbol( s.class ) );

    assertEquals( "px", Units.getSymbol( px.class ) );
    assertEquals( "°", Units.getSymbol( deg.class ) );
  }

  @Test
  public void testNames() throws Exception {
    assertEquals( "metre", Units.getName( m.class ) );
    assertEquals( "centimetre", Units.getName( cm.class ) );
    assertEquals( "kilogram", Units.getName( kg.class ) );
    assertEquals( "square metre", Units.getName( m2.class ) );
    assertEquals( "metres per second", Units.getName( m_s.class ) );
    assertEquals( "second", Units.getName( s.class ) );
    assertEquals( "degree", Units.getName( deg.class ) );
  }

  @Test
  public void testReflection() throws Exception {
    Method method = UnitTest.class.getMethod( "calcMetres", Double.TYPE );
    assertNotNull( method );

    assertEquals( 1, method.getParameterAnnotations().length );
    assertEquals( 1, method.getParameterAnnotations()[0].length );
    Annotation annotation = method.getParameterAnnotations()[0][0];
    assertEquals( "com.cedarsoft.unit.si.m", annotation.annotationType().getName() );

    m metre = ( m ) annotation;

    assertTrue( annotation.annotationType().isAnnotationPresent( Length.class ) );
    assertTrue( annotation.annotationType().isAnnotationPresent( SIBaseUnit.class ) );

    assertTrue( annotation.annotationType().isAnnotationPresent( SIBaseUnit.class ) );
    assertFalse( annotation.annotationType().isAnnotationPresent( SiDerivedUnit.class ) );

    assertEquals( "m", annotation.annotationType().getAnnotation( Symbol.class ).value() );
  }

  @Test
  public void testReflCm() throws Exception {
    Method method = UnitTest.class.getMethod( "convertToMetres", Double.TYPE );
    assertNotNull( method );

    assertEquals( 1, method.getParameterAnnotations().length );
    assertEquals( 1, method.getParameterAnnotations()[0].length );

    Annotation annotation = method.getParameterAnnotations()[0][0];
    assertEquals( "com.cedarsoft.unit.si.cm", annotation.annotationType().getName() );

    assertTrue( annotation.annotationType().isAnnotationPresent( Length.class ) );
    assertTrue( annotation.annotationType().isAnnotationPresent( centi.class ) );
    assertFalse( annotation.annotationType().isAnnotationPresent( SiDerivedUnit.class ) );
    assertFalse( annotation.annotationType().isAnnotationPresent( SIBaseUnit.class ) );

    assertEquals( "cm", annotation.annotationType().getAnnotation( Symbol.class ).value() );
  }
}

