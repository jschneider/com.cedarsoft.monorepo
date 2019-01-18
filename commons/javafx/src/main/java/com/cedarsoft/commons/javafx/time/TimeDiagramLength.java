package com.cedarsoft.commons.javafx.time;

import com.cedarsoft.unit.si.s;

/**
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public enum TimeDiagramLength {
  SECONDS_15(15),
  SECONDS_30(30),
  SECONDS_60(60);

  @s
  private final int length;

  TimeDiagramLength(@s int length) {
    this.length = length;
  }

  @s
  public int getSeconds() {
    return length;
  }
}
