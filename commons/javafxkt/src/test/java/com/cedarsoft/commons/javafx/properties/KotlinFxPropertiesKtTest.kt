package com.cedarsoft.commons.javafx.properties

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class KotlinFxPropertiesKtTest {
  @Test
  internal fun name() {
    val foo = Foo()

    assertThat(foo.name).isEqualTo("")
    foo.name = "asdf"
    assertThat(foo.name).isEqualTo("asdf")
    assertThat(foo.nameProperty.get()).isEqualTo("asdf")
  }
}

class Foo {
  val nameProperty: StringProperty = SimpleStringProperty("")
  var name: String by nameProperty

  val ageProperty: DoubleProperty = SimpleDoubleProperty(7.0)
  var age: Double by ageProperty

  val age2Property: IntegerProperty = SimpleIntegerProperty(7)
  var age2: Int by age2Property
}
