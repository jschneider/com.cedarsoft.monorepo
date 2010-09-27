package com.cedarsoft;

import com.cedarsoft.unit.CentiMetre;
import com.cedarsoft.unit.Length;
import com.cedarsoft.unit.Metre;
import org.jetbrains.annotations.NotNull;
import org.junit.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 *
 */
public class UnitTest {
  @Test
  public void testIt() {
    Unit<com.cedarsoft.quantity.Length> metreUnit = MetricSystem.METRE;

    {
      @Metre
      double metre = 7;
      @Metre
      double result = calcMetres( metre );
    }

    @Metre
    double result = convertToMetres( 37, MetricSystem.CENTI_METRE );
  }

  @Metre
  public static double calcMetres( @Metre double amount ) {
    return amount;
  }

  @Metre
  public static double convertToMetres( @Length double amount, @NotNull Unit<? extends com.cedarsoft.quantity.Length> unit ) {
    if ( unit == MetricSystem.METRE ) {
      return amount;
    }
    if ( unit == MetricSystem.CENTI_METRE ) {
      return amount / 100.0;
    }

    throw new UnsupportedOperationException( "Unsupported unit: " + unit );
  }

  @Metre
  public static double convertToMetres( @CentiMetre double centiMetres ) {
    return centiMetres / 100.0;
  }

  @Test
  public void testConvert() throws Exception {
    @CentiMetre
    int cm = 100;
    @Metre
    double result = convertToMetres( cm );
    assertEquals( 1, result, 0 );
  }

  @Test
  public void testConvert2() throws Exception {
    @CentiMetre
    int cm = 100;
    @Metre
    double result = convertToMetres( cm, MetricSystem.CENTI_METRE );
    assertEquals( 1, result, 0 );
  }

  @Test
  public void testUnits() throws Exception {
    assertEquals( "m", MetricSystem.METRE.getSymbol() );
    assertEquals( "cm", MetricSystem.CENTI_METRE.getSymbol() );
  }

  @Test
  public void testReflection() throws Exception {
    Method method = UnitTest.class.getMethod( "calcMetres", Double.TYPE );
    assertNotNull( method );

    assertEquals( 1, method.getParameterAnnotations().length );
    assertEquals( 1, method.getParameterAnnotations()[0].length );
    Annotation annotation = method.getParameterAnnotations()[0][0];
    assertEquals( "com.cedarsoft.unit.Metre", annotation.annotationType().getName() );

    Metre metre = ( Metre ) annotation;
    assertEquals( "daValue", metre.value() );
  }
}

