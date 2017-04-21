package com.cedarsoft.tiles;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MovementSpeedCalculatorTest {
  @Test
  public void testIt() throws Exception {
    MovementSpeedCalculator calculator = new MovementSpeedCalculator();

    calculator.add(1, 1, 1, 99);
    calculator.add(1, 1, 1, 99);
    calculator.add(1, 1, 1, 99);
    calculator.add(1, 1, 1, 99);

    assertThat(calculator.calculateSpeedX()).isBetween(0.63, 0.64);
    assertThat(calculator.calculateSpeedY()).isBetween(0.63, 0.64);
  }
}