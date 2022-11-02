package com.cedarsoft.observable

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 */
class DependentObjectsTest {
  @Test
  internal fun testAvoidGc() {
    val observableObject = ObservableObject("asdf")

    val ref = createAndRegisterObject(observableObject)
    assertThat(ref.get()).isNotNull()

    gc()

    //Must not be null, because the instance is stored
    assertThat(ref.get()).isNotNull()

    observableObject.removeDependentObject("key")

    forceGc {
      ref.get() == null
    }

    assertThat(ref.get()).isNull()
  }

  /**
   * Creates an object and registers it as dependent object.
   * Returns the object as weak reference
   */
  private fun createAndRegisterObject(observableObject: ObservableObject<String>): WeakReference<MyClass> {
    val referent = MyClass("bla")
    observableObject.addDependentObject("key", referent)
    return WeakReference(referent)
  }


  data class MyClass(val name: String)
}
