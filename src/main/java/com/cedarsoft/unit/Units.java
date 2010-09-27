package com.cedarsoft.unit;

import com.cedarsoft.unit.other.deg;
import com.cedarsoft.unit.si.rad;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * @param <Q> the quantity
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Units {
  private Units() {
  }

  @NotNull
  @NonNls
  public static String getSymbol( Class<? extends Annotation> unitType ) {
    Symbol symbol = unitType.getAnnotation( Symbol.class );
    if ( symbol == null ) {
      throw new IllegalArgumentException( "Missing annotation " + Symbol.class.getName() + " for " + unitType );
    }

    return symbol.value();
  }

  @NotNull
  @NonNls
  public static String getName( Class<? extends Annotation> unitType ) {
    Name name = unitType.getAnnotation( Name.class );
    if ( name == null ) {
      throw new IllegalArgumentException( "Missing annotation " + Name.class.getName() + " for " + unitType );
    }

    return name.value();
  }
}