package com.cedarsoft.test.tornadofx.view

import tornadofx.*

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main(args: Array<String>) {
  launch<PersonApp>(*args)
}

class PersonApp : App(CombinedView::class) {
  init {
    importStylesheet("/bootstrap3.css")
  }
}