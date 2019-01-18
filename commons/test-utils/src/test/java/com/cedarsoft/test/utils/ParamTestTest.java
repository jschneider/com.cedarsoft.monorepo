package com.cedarsoft.test.utils;

import static org.assertj.core.api.Assertions.*;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParamTestTest {
  @ParameterizedTest
  @MethodSource("dataProvider")
  void daTest(@Nonnull String candidate, int val) {
    assertThat(Integer.parseInt(candidate)).isEqualTo(val);
  }

  @Nonnull
  public Arguments[] dataProvider() {
    return new Arguments[]{
      Arguments.of("1", 1),
      Arguments.of("2", 2),
      Arguments.of("3", 3),
      };
  }
}
