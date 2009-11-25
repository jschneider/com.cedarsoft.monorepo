package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 * A condition.
 */
public interface Condition {
  /**
   * Static condition that is always false.
   */
  @NotNull
  Condition FALSE = new Condition() {
    @Override
    public boolean isValid() {
      return false;
    }
  };
  /**
   * Static condition that is always true.
   */
  @NotNull
  Condition TRUE = new Condition() {
    @Override
    public boolean isValid() {
      return true;
    }
  };

  boolean isValid();
}