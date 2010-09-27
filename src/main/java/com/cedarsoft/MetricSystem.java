package com.cedarsoft;

import com.cedarsoft.quantity.Area;
import com.cedarsoft.quantity.Length;
import com.cedarsoft.unit.cm;
import com.cedarsoft.unit.m;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MetricSystem {
  @NotNull
  public static final Unit<Length> METRE = new BaseUnit<Length>( "m", m.class );

  @NotNull
  public static final Unit<Length> CENTI_METRE = new BaseUnit<Length>( "cm", cm.class );


  @NotNull
  public static final Unit<Area> SQUARE_METRE = new BaseUnit<Area>( "m²", m.class );


}
