package com.cedarsoft.tests.geolocation

import com.cedarsoft.geolocation.Coords
import org.junit.jupiter.api.Test

/**
 * Breite (Nord/Süd) / Länge (Ost/West)
 * Latitude          / Longitude
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class GeoLocationTest {
  @Test
  internal fun testIt() {
    val erdgasTankstelle = Coords(48.45496, 9.08439)
    val lindenWasen = Coords(48.44782, 9.09738)
    val lindachstraße = Coords(48.44831, 9.09759)

    println("Lindenwasen zu lindachstraße: ${lindenWasen.calculateDistanceTo(lindachstraße)}")
    println("Lindenwasen zu erdgastankstelle: ${lindenWasen.calculateDistanceTo(erdgasTankstelle)}")
  }
}
