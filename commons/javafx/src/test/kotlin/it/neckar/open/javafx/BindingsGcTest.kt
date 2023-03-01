package it.neckar.open.javafx

import it.neckar.open.javafx.test.JavaFxTest
import it.neckar.open.test.utils.*
import javafx.beans.binding.DoubleBinding
import javafx.beans.binding.StringBinding
import javafx.beans.property.SimpleDoubleProperty
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

@Disabled //for performance reasons
@JavaFxTest
class BindingsGcTest {
  @Test
  fun testOnlyBinding() {
    val property = SimpleDoubleProperty(17.0)
    val ref = createRef(property)

    //We create a binding - that is *not* held and should be garbage collected
    forceGc {
      ref.get() == null
    }
  }

  @Test
  fun testBindingWithTextField() {
    val property = SimpleDoubleProperty(17.0)

    val ref = createRef(property)

    val textField = Components.textFieldDoubleReadonly(ref.get()!!)
    //Should *not* be garbage collected
    try {
      forceGc {
        ref.get() == null
      }

      throw AssertionError("Should not be garbage collected, but was")
    } catch (e: IllegalStateException) {
    }
  }

  @Test
  fun testBindingWithTextFieldStringProperty() {
    val property = SimpleDoubleProperty(17.0)

    val ref = createRefAsString(property)

    val textField = Components.textFieldReadonly(ref.get()!!)
    //Should *not* be garbage collected
    try {
      forceGc {
        ref.get() == null
      }

      throw AssertionError("Should not be garbage collected, but was")
    } catch (e: IllegalStateException) {
    }
  }
}

private fun createRef(property: SimpleDoubleProperty): WeakReference<DoubleBinding> {
  return WeakReference(property.divide(100.0))
}

private fun createRefAsString(property: SimpleDoubleProperty): WeakReference<StringBinding> {
  return WeakReference(property.asString())
}
