package com.cedarsoft.commons.javafx;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

class BidirectionalBindingTest {

  @Test
  void bindBidirectional() {

    DoubleProperty lengthCommunication = new SimpleDoubleProperty(0);
    DoubleProperty lengthUI = new SimpleDoubleProperty(0);

    DoubleProperty offsetProperty = new SimpleDoubleProperty(0);

    final int[] updateCounter = {0};

    BidirectionalBinding.bindBidirectional(lengthCommunication,
                                           lengthUI,
                                           (observable, oldValue, newValue) -> {
                                             updateCounter[0]++;
                                             assertEquals(newValue, lengthCommunication.get());
                                             lengthUI.setValue((lengthCommunication.get() + offsetProperty.get()) / 10);
                                           },
                                           (observable, oldValue, newValue) -> {
                                             updateCounter[0]++;
                                             assertEquals(newValue, lengthUI.get());
                                             lengthCommunication.setValue(lengthUI.get() * 10 - offsetProperty.get());
                                           },
                                           offsetProperty);

    assertThat(lengthCommunication.get()).isEqualTo(0.0);
    assertThat(lengthUI.get()).isEqualTo(0.0);

    lengthCommunication.set(10.0);
    assertThat(lengthCommunication.get()).isEqualTo(10.0);
    assertThat(lengthUI.get()).isEqualTo(1.0);

    offsetProperty.setValue(20.0);
    assertThat(lengthCommunication.get()).isEqualTo(10.0);
    assertThat(lengthUI.get()).isEqualTo(3.0);

    lengthUI.setValue(5.0);
    assertThat(lengthCommunication.get()).isEqualTo(30.0);
    assertThat(lengthUI.get()).isEqualTo(5.0);

  }
}
