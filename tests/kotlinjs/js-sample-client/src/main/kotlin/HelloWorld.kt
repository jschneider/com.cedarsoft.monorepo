package com.cedarsoft.tests.kotlin.js

import kotlin.browser.document

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main() {
  println("Hello World is executed!!!")
}

fun myApp() {
  println("myApp is called")

  document.bgColor = "FFAA12"

  val element = document.getElementById("foo")
  if (element != null) {
    val p = Pair(10, 20)
    element.appendChild(document.createTextNode("Hello you there ${p.first}"))
  }
}
