package it.neckar.open.javafx.properties

import assertk.*
import assertk.assertions.*
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
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

  @Test
  internal fun testSetMultipleTimes() {
    val foo = Foo()
    foo.name = "daName"

    val called = AtomicBoolean()

    foo.nameProperty.addListener(object : ChangeListener<String?> {
      override fun changed(observable: ObservableValue<out String?>?, oldValue: String?, newValue: String?) {
        called.set(true)
      }
    })


    assertThat(called.get()).isFalse()
    //Same name, should not trigger the listener
    foo.name = "daName"
    assertThat(called.get()).isFalse()
    foo.name = "daName2"
    assertThat(called.get()).isTrue()
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
