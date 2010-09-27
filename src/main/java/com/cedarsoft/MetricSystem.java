package com.cedarsoft;

import com.cedarsoft.quantity.Length;
import com.cedarsoft.unit.CentiMetre;
import com.cedarsoft.unit.Metre;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MetricSystem {
  @NotNull
  public static final Unit<Length> METRE = new BaseUnit<Length>( "m", Metre.class );

  @NotNull
  public static final Unit<Length> CENTI_METRE = new BaseUnit<Length>( "cm", CentiMetre.class );

}
