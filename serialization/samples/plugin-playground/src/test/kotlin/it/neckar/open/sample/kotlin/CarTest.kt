package com.cedarsoft.sample.kotlin

import assertk.assertThat
import com.cedarsoft.sample.Model
import com.cedarsoft.sample.Money
import hasModel
import org.junit.jupiter.api.Test

internal class CarTest {
  @Test
  fun name() {
    val car = Car(Model("Model 3"), Money(36_000.0))
    assertThat(car).hasModel(Model("Model 3"))
  }
}
