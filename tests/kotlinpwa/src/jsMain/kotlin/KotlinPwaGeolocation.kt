package com.cedarsoft.tests.pwa

import com.cedarsoft.open.kotlinpwa.common.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.w3c.dom.*
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.dom.createElement
import kotlin.js.Date
import kotlin.math.roundToInt


/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
private val model = ClientModel()

fun main(args: Array<String>) {
  println("Starting geolocation in Browser")
  startPwaDemo()
}

fun startPwaDemo() {
  println("on console: startPwaDemo")
  log("Loading...")

  val navigator: Navigator = window.navigator

  val dyn: dynamic = navigator
  val geolocation = dyn.geolocation

  println("service worker: ${navigator.serviceWorker}")
  val geolocationOptions = js("""{enableHighAccuracy: true, timeout: 5000, maximumAge: 6000}""")

  val refreshButton = getElementSafe("refreshButton") as HTMLButtonElement
  refreshButton.onclick = { _ ->
    geolocation.getCurrentPosition(::handleOwnPositionUpdate, ::handleOwnPositionFailure, geolocationOptions)
    //geolocation.getCurrentPosition(::handleOwnPositionUpdate, ::handleOwnPositionFailure, geolocationOptions)
    refreshPositionReport()
  }

  val publishButton = getElementSafe("publishButton") as HTMLButtonElement
  publishButton.onclick = { _ ->
    publishPosition()
  }

  //Get positions initially
  //geolocation.getCurrentPosition(::handleOwnPositionUpdate, ::handleOwnPositionFailure, geolocationOptions)
  geolocation.watchPosition(::handleOwnPositionUpdate, ::handleOwnPositionFailure, geolocationOptions)
  refreshPositionReport()
}

private fun log(message: String) {
  val debugOutput = getElementSafe("debugOutput")
  debugOutput.textContent = "$message"
}

private fun getElementSafe(id: String): Element {
  val element = document.getElementById(id)

  if (element == null) {
    println("No element found for id <$id>")
    throw IllegalArgumentException("No element found for <$id>")
  }
  return element
}

fun handleOwnPositionFailure(position: dynamic) {

  val code = (position.unsafeCast<JsPositionError>()).code
  val message = (position.unsafeCast<JsPositionError>()).message

  val codeAsString = when (code) {
    1    -> "Permission denied"
    2    -> "Position unavailable"
    3    -> "Timeout"
    else -> "Unknown error $code"
  }

  println("Failed $codeAsString  - $message")
  log("Failed fetching position due to $codeAsString - $message")
}

fun handleOwnPositionUpdate(position: dynamic) {
  val jsPosition = position.unsafeCast<JsPosition>()

  val myPosition = Coords(jsPosition.coords.latitude, jsPosition.coords.longitude)
  model.myCoords = myPosition
  updateUi()

  getElementSafe("myPosition").apply {
    textContent = "Genauigkeit: ${jsPosition.coords.accuracy.format1Digit()} m"
  }

  getElementSafe("myPositionLink").apply {
    setAttribute("href", myPosition.createOpenStreetMapLink())
  }
}

private fun Coords.createOpenStreetMapLink() = "https://www.openstreetmap.org/?mlat=$latitude&mlon=$longitude"

private fun Coords.format(): String {
  return "$latitude $longitude"
}

/**
 * Gets the latest result from the server and updates the UI
 */
private fun refreshPositionReport() {
  log("Lade Ziel-Koordinaten")

  GlobalScope.launch {
    val result = httpGet("positions")
    model.positionReport = Json.parse(PositionReport.serializer(), result)
    log("Ziel-Koordinaten geladen")
    updateUi()
  }
}

fun publishPosition(): Unit {
  log("Veröffentliche Position")
  val myName = (getElementSafe("myName") as HTMLInputElement).value

  GlobalScope.launch {
    val coords = model.myCoords
    if (coords == null) {
      log("Eigene Position nicht verfügbar")
      return@launch
    }

    val json = Json.stringify(PositionInfo.serializer(), PositionInfo(UserInfo(myName), coords, Date().getUTCMilliseconds().toLong()))
    val result = httpPutOrPost("positions", json, HTTPVerbs.POST)

    log("Eigene Koordinaten veröffentlicht: $result")

    //Update later
    refreshPositionReport()
  }
}

fun updateUi() {
  val positionsDiv = getElementSafe("positionsDiv") as HTMLDivElement

  var firstChild = positionsDiv.firstChild
  while (firstChild != null) {
    positionsDiv.removeChild(firstChild)
    firstChild = positionsDiv.firstChild
  }

  //Add the segments for the position report
  model.positionReport.positionInfos
    .forEach { positionInfo ->
      val distance = model.myCoords?.calculateDistanceTo(positionInfo.coords)

      val div = document.createElement("div") {
        classList.add("positionDiv")
      }.also {
        positionsDiv.appendChild(it)
      }

      document.createElement("div") {
        classList.add("userInfoDiv")
        textContent = positionInfo.userInfo.name
      }.also {
        div.appendChild(it)
      }

      document.createElement("div") {
        classList.add("distanceDiv")
        textContent = "${distance?.format()}"
      }.also {
        div.appendChild(it)
      }

      //Add a link to OpenStreetMap
      document.createElement("a") {
        setAttribute("href", positionInfo.coords.createOpenStreetMapLink())
        setAttribute("target", "openStreetMap")
        textContent = "Karte"
      }.also {
        div.appendChild(it)
      }

      document.createElement("div") {
        classList.add("lastUpdatedDiv")
        textContent = "Zuletzt gesichtet vor: ${positionInfo.updatedTime.formatDeltaTime()}"
      }.also {
        div.appendChild(it)
      }
    }

  getElementSafe("lastUpdateTime").apply {
    val formatted = model.positionReport.updateTime.formatTime()
    textContent = "Zuletzt aktualisiert: ${formatted}"
  }
}

private fun Long.formatTime(): String {
  return Date(this).toLocaleTimeString(locales = "de")
}

private fun Long.formatDeltaTime(): String {
  val delta = Date.now() - this

  val secondsTotal = delta / 1000.0
  val minutesTotal = secondsTotal / 60.0

  val minutesFull = minutesTotal.toInt()
  val seconds = (secondsTotal - minutesFull * 60).roundToInt()

  return "$minutesFull Minuten $seconds Sekunden"
}

private fun Distance.format(): String {
  var formatted = ""

  if (north >= 0) {
    formatted += "Nach Norden: ${north.format1Digit()} m. "
  } else {
    formatted += "Nach Süden: ${(-north).format1Digit()} m. "
  }

  if (east >= 0) {
    formatted += "Nach Osten: ${east.format1Digit()} m. "
  } else {
    formatted += "Nach Westen: ${(-east).format1Digit()} m. "
  }

  formatted += "Entfernung: ${total.format1Digit()} m"

  return formatted
}

private fun Double.format1Digit(): String {
  return this.asDynamic().toFixed(1) as String
}

external interface JsPosition {
  val coords: JsCoordinates
  val timestamp: Long
}

external interface JsPositionError {
  val code: Int
  val message: String
}

external interface JsCoordinates {
  val latitude: Double
  val longitude: Double
  val altitude: Double
  val accuracy: Double
  val altitudeAccuracy: Double
  val heading: Double
  val speed: Double
}

suspend fun httpPutOrPost(url: String, data: String, httpVerb: HTTPVerbs): String = suspendCoroutine { c ->
  when (httpVerb) {
    HTTPVerbs.POST, HTTPVerbs.PUT -> {
      val xhr = XMLHttpRequest()
      xhr.onreadystatechange = { _ -> statusHandler(xhr, c) }
      xhr.onerror = { _ -> log("Error: ${xhr.responseText}") }
      xhr.onabort = { _ -> log("Aborted: ${xhr.responseText}") }
      xhr.open(httpVerb.name, url, true)
      xhr.setRequestHeader("Content-type", "application/json; charset=utf-8")
      xhr.send(data)
    }
    else                          -> console.log("An unsupported verb was passed through to this function")
  }
}

suspend fun httpGet(url: String): String = suspendCoroutine { c ->
  val xhr = XMLHttpRequest()
  xhr.onreadystatechange = { _ -> statusHandler(xhr, c) }
  xhr.open("GET", url)
  xhr.send()
}

fun statusHandler(xhr: XMLHttpRequest, coroutineContext: Continuation<String>) {
  if (xhr.readyState == XMLHttpRequest.DONE) {
    if (xhr.status / 100 == 2) {
      coroutineContext.resume(xhr.response as String)
    } else {
      coroutineContext.resumeWithException(RuntimeException("HTTP error: ${xhr.status}"))
    }
  } else {
    null
  }
}

enum class HTTPVerbs {
  POST,
  GET,
  PUT,
  UPDATE,
  DELETE
}
