package com.cedarsoft.commons.javafx;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import javafx.beans.property.SimpleBooleanProperty;

class OverrideBindingTest {

  @Test
  void testOverrideBinding() {
    SimpleBooleanProperty myProperty = new SimpleBooleanProperty();
    SimpleBooleanProperty aProperty = new SimpleBooleanProperty();
    SimpleBooleanProperty bProperty = new SimpleBooleanProperty();

    assertThat(myProperty.get()).isFalse();
    assertThat(aProperty.get()).isFalse();
    assertThat(bProperty.get()).isFalse();

    myProperty.bind(aProperty);
    assertThat(myProperty.get()).isFalse();

    myProperty.bind(bProperty);
    assertThat(myProperty.get()).isFalse();

    aProperty.set(true);
    assertThat(myProperty.get()).isFalse();

    bProperty.set(true);
    assertThat(myProperty.get()).isTrue();
  }
}
