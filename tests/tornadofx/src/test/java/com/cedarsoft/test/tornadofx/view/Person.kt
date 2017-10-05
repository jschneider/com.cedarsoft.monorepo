package com.cedarsoft.test.tornadofx.view

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Person(name: String = "", title: String = "") {
  val nameProperty = SimpleStringProperty(this, "name", name)
  var name by nameProperty

  val titleProperty = SimpleStringProperty(this, "title", title)
  var title by titleProperty
}