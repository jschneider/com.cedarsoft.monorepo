package com.cedarsoft.test.tornadofx.view

import tornadofx.*

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class PersonController : Controller() {
  val persons = listOf(
    Person("John", "Manager"),
    Person("Jay", "Worker bee"),
    Person("Joda", "Master")
  ).observable()

}
