package com.cedarsoft.tests.pwa

import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.Navigator
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.createElement


/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */

val home = Coords(48.44782, 9.09738)

fun main(args: Array<String>) {
  println("Hello, world! in Main!")
  startPwaDemo()
}

fun startPwaDemo() {
  println("on console: startPwaDemo")

  val element = document.getElementById("myp") as HTMLParagraphElement
  println("element: $element - ${element.className}")

  element.textContent = "Da text from Kotlin"

  println("Active element: ${document.activeElement}")
  println("Root element: ${document.rootElement}")


  val navigator: Navigator = window.navigator

  val dyn: dynamic = navigator
  val geolocation = dyn.geolocation
  println("geo $geolocation")
  geolocation.getCurrentPosition(::showPosition)

  //navigator.geolocation.getCurrentPosition()

  println("service worker: ${navigator.serviceWorker}")
}

fun showPosition(position: dynamic) {
  println("showPosition: $position")
  println("showPosition: ${position.coords} ")
  println("showPosition: ${position.timestamp} ")

  val p = position.unsafeCast<Position>()
  println("p: $p")
  println("p: ${p.timestamp}")
  println("p.alt: ${p.coords.altitude}")
  println("p.altAccuracy: ${p.coords.altitudeAccuracy}")
  println("p.latitude: ${p.coords.latitude}")
  println("p.longitude: ${p.coords.longitude}")
  println("p.accuracy: ${p.coords.accuracy}")
  println("p.accuracy: ${p.coords.accuracy}")
  println("p.speed: ${p.coords.speed}")


  val myPosition = Coords(p.coords.latitude, p.coords.longitude)
  println("myPosition: $myPosition")

  val distance = home.calculateDistanceTo(myPosition)
  println("Richtung Heimat:: ${distance.north} m nach Norden un ${distance.east} m nach Osten")

  val paragraph = document.getElementById("myp") as HTMLParagraphElement
  paragraph.textContent = "Richtung Heimat:: ${distance.north} m nach Norden und ${distance.east} m nach Osten"


  document.createElement("a") {
    this.setAttribute("href", "https://www.openstreetmap.org/search?query=${myPosition.latitude} ${myPosition.longitude}")
    this.textContent = "OpenStreetMap"
  }.also {
    paragraph.appendChild(it)
  }
}

external interface Position {
  val coords: Coordinates
  val timestamp: Long
}

external interface Coordinates {
  val latitude: Double
  val longitude: Double
  val altitude: Double
  val accuracy: Double
  val altitudeAccuracy: Double
  val heading: Double
  val speed: Double
}
