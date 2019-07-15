package com.cedarsoft.tests.pwa.server

import com.cedarsoft.open.kotlinpwa.common.PositionInfo
import com.cedarsoft.open.kotlinpwa.common.PositionReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.cedarsoft.ktor.client.createHttpClient
import io.ktor.client.request.post


fun main() {
  val json = """

{"updateTime":1563027728185,"positionInfos":[{"userInfo":{"name":"Geheime Nachricht"},"coords":{"latitude":48.62137356311886,"longitude":9.148049019285615},"updatedTime":1563027158931},{"userInfo":{"name":"Königsweg Shuttle"},"coords":{"latitude":48.6258272,"longitude":9.1495292},"updatedTime":1563025602068},{"userInfo":{"name":"Naboo"},"coords":{"latitude":48.6246844,"longitude":9.1447713},"updatedTime":1563026964032},{"userInfo":{"name":"Pilotenschein"},"coords":{"latitude":48.6191577,"longitude":9.1472928},"updatedTime":1563024387280},{"userInfo":{"name":"Planet Melmac"},"coords":{"latitude":48.6211071,"longitude":9.1470724},"updatedTime":1563024078345},{"userInfo":{"name":"Space Siggi"},"coords":{"latitude":48.62221925,"longitude":9.14445758},"updatedTime":1563024056831},{"userInfo":{"name":"Uli am Toten Baum"},"coords":{"latitude":48.62615786963014,"longitude":9.147740062334712},"updatedTime":1563025133077},{"userInfo":{"name":"Zeltplatz"},"coords":{"latitude":48.624087,"longitude":9.145963},"updatedTime":1563024019253}]}

""".trimIndent()

  val report = Json.parse(PositionReport.serializer(), json)

  println("Report: $report")
  println("Report: ${report.positionInfos}")
  val client = createHttpClient()
  report.positionInfos.forEach { positionInfo ->
    GlobalScope.launch {
      val result = client.post<String>("https://vakila.cedarsoft.com/positions22") {
        body = Json.stringify(PositionInfo.serializer(), positionInfo)
      }
      println("Result: $result")
    }
  }

  Thread.sleep(5000)
}
