package com.cedarsoft.commons.javafx.properties;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 */
public class KotlinFxPropertiesJavaTest {
  @Test
  void name() {
    Foo foo = new Foo();
    assertThat(foo.getName()).isEqualTo("");
    foo.setName("asdf");
    assertThat(foo.getName()).isEqualTo("asdf");
    assertThat(foo.getNameProperty().get()).isEqualTo("asdf");

    foo.getAge();
    foo.setAge(17);
    foo.getAgeProperty();
  }
}
