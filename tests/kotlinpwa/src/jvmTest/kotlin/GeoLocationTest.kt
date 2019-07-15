package com.cedarsoft.tests.geolocation

import com.cedarsoft.ktor.client.createHttpClient
import com.cedarsoft.open.kotlinpwa.common.Coords
import com.cedarsoft.open.kotlinpwa.common.PositionInfo
import com.cedarsoft.open.kotlinpwa.common.PositionReport
import io.ktor.client.request.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withinPercentage
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
    val lindenwasen = Coords(48.44782, 9.09738)
    val lindachstraße = Coords(48.44831, 9.09759)

    lindenwasen.calculateDistanceTo(lindachstraße).also {
      assertThat(it.north).isCloseTo(55.4, withinPercentage(0.1))
      assertThat(it.east).isCloseTo(15.5, withinPercentage(0.1))
    }

    lindenwasen.calculateDistanceTo(erdgasTankstelle).also {
      assertThat(it.north).isCloseTo(807.5, withinPercentage(0.1))
      assertThat(it.east).isCloseTo(-958.9, withinPercentage(0.1))
    }
  }

  @Test
  internal fun testEntry() {
    val json = """

{"updateTime":1563025245117,"positionInfos":[{"userInfo":{"name":"Königsweg Shuttle"},"coords":{"latitude":48.6262003,"longitude":9.1478341},"updatedTime":1563024076395},{"userInfo":{"name":"Naboo"},"coords":{"latitude":48.6249655,"longitude":9.1424293},"updatedTime":1563024109400},{"userInfo":{"name":"Pilotenschein"},"coords":{"latitude":48.6191577,"longitude":9.1472928},"updatedTime":1563024387280},{"userInfo":{"name":"Planet Melmac"},"coords":{"latitude":48.6211071,"longitude":9.1470724},"updatedTime":1563024078345},{"userInfo":{"name":"Space Siggi"},"coords":{"latitude":48.62221925,"longitude":9.14445758},"updatedTime":1563024056831},{"userInfo":{"name":"Uli am Toten Baum"},"coords":{"latitude":48.62615786963014,"longitude":9.147740062334712},"updatedTime":1563025133077},{"userInfo":{"name":"Zeltplatz"},"coords":{"latitude":48.624087,"longitude":9.145963},"updatedTime":1563024019253}]}

""".trimIndent()

    val report = Json.parse(PositionReport.serializer(), json)

    println("Report: $report")
    println("Report: ${report.positionInfos}")
    val client = createHttpClient()
    report.positionInfos.forEach { positionInfo ->
      GlobalScope.launch {
        client.post<String>("localhost:8099/positions") {
          body = Json.stringify(PositionInfo.serializer(), positionInfo)
        }
      }
    }
  }
}
