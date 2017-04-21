package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.unit.other.px;

/**
 * How large the step is, when panning
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public enum PanningStep {
  NORMAL(10),
  LARGE(50);

  private final int size;

  PanningStep(@px int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }
}
