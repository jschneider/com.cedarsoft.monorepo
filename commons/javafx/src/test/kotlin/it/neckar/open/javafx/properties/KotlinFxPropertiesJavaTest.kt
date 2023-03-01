package it.neckar.open.javafx.properties

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 */
class KotlinFxPropertiesJavaTest {
  @Test
  fun name() {
    val foo = Foo()
    assertThat(foo.name).isEqualTo("")
    foo.name = "asdf"
    assertThat(foo.name).isEqualTo("asdf")
    assertThat(foo.nameProperty.get()).isEqualTo("asdf")
    foo.age
    foo.age = 17.0
    foo.ageProperty
  }
}
