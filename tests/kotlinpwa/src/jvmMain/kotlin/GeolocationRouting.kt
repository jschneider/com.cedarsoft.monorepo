package com.cedarsoft.tests.pwa.server

import com.cedarsoft.open.kotlinpwa.common.Coords
import com.cedarsoft.open.kotlinpwa.common.PositionInfo
import com.cedarsoft.open.kotlinpwa.common.PositionReport
import com.cedarsoft.open.kotlinpwa.common.UserInfo
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import kotlinx.serialization.json.Json
import java.util.Locale

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
private val lindenwasen = Coords(48.44782, 9.09738)
private val erdgasTankstelle = Coords(48.45496, 9.08439)
private val lindachstraße = Coords(48.44831, 9.09759)
private val zeltplatz = Coords(48.624087, 9.145963)

private val model = ServerModel().apply {
  this.update(PositionInfo(UserInfo("Zeltplatz"), zeltplatz, System.currentTimeMillis()))
}

private fun randomCoords(): Coords {
  return Coords(lindenwasen.latitude + Math.random() - 0.5, lindenwasen.longitude + Math.random() - 0.5)
}

class GeolocationRouting {
  val modelRouting: Routing.() -> Unit = {
    route("positions") {
      get {

        val report = PositionReport(
          System.currentTimeMillis(),
          model.positionInfosSorted()
        )
        val json = Json.stringify(PositionReport.serializer(), report)

        call.respond(HttpStatusCode.OK, json)
      }

      delete("{id}") {
        val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
        println("Deleting Id: $id")
        model.delete(UserInfo(id))
        call.respond(HttpStatusCode.OK)
        return@delete
      }

      post {
        val json = context.request.call.receiveText()
        println("got updated position: $json")
        val positionInfo = Json.parse(PositionInfo.serializer(), json)

        model.update(positionInfo.copy(updatedTime = System.currentTimeMillis()))
        call.respond(HttpStatusCode.OK)
      }
    }

    static("") {
      defaultResource("index.html")
    }

    static("/static") {
      resource("com.cedarsoft.monorepo-kotlinpwa.js")
    }
  }
}

class ServerModel {
  private val positionInfos = mutableMapOf<UserInfo, PositionInfo>()

  fun update(positionInfo: PositionInfo) {
    positionInfos[positionInfo.userInfo] = positionInfo
  }

  fun positionInfosSorted(): List<PositionInfo> {
    val all = mutableListOf<PositionInfo>()
    all.addAll(positionInfos.values)

    //all.add(PositionInfo(UserInfo("JS"), lindenwasen, System.currentTimeMillis()))
    //all.add(PositionInfo(UserInfo("Erdgas"), erdgasTankstelle, System.currentTimeMillis()))
    //all.add(PositionInfo(UserInfo("Lindach"), lindachstraße, System.currentTimeMillis()))
    //all.add(PositionInfo(UserInfo("Random"), randomCoords(), System.currentTimeMillis()))

    all.sortBy {
      it.userInfo.name.toLowerCase(Locale.GERMAN)
    }

    return all
  }

  fun delete(userInfo: UserInfo) {
    positionInfos.remove(userInfo)
  }
}
