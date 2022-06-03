/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.swing.components.timeline;

import javax.annotation.Nonnull;

/**
 */
public class ConstantBarValues implements TimelineModel.BarValues {
  private final double critical;
  private final double warnings;
  private final double neutral;

  public ConstantBarValues(double neutral, double warnings, double critical) {
    this.critical = critical;
    this.warnings = warnings;
    this.neutral = neutral;
  }

  @Override
  public double getValueCritical() {
    return critical;
  }

  @Override
  public double getValueWarnings() {
    return warnings;
  }

  @Override
  public double getValueNeutral() {
    return neutral;
  }

  @Override
  public String toString() {
    return "StaticSegmentContent{" +
      "critical=" + critical +
      ", warnings=" + warnings +
      ", neutral=" + neutral +
      '}';
  }

  public static final class Builder {
    private double critical;
    private double warnings;
    private double neutral;

    public Builder() {
    }

    public Builder(double critical, double warnings, double neutral) {
      this.critical = critical;
      this.warnings = warnings;
      this.neutral = neutral;
    }

    @Nonnull
    public Builder setCritical(double critical) {
      this.critical = critical;
      return this;
    }

    @Nonnull
    public Builder setWarnings(double warnings) {
      this.warnings = warnings;
      return this;
    }

    @Nonnull
    public Builder setNeutral(double neutral) {
      this.neutral = neutral;
      return this;
    }

    @Nonnull
    public ConstantBarValues build() {
      return new ConstantBarValues(neutral, warnings, critical);
    }

    public void increaseWarning() {
      warnings++;
    }

    public void increaseCritical() {
      critical++;
    }

    public void increaseNeutral() {
      neutral++;
    }

    public void increase(@Nonnull TimelineModel.Severity severity) {
      switch (severity) {
        case CRITICAL:
          increaseCritical();
          return;
        case WARNING:
          increaseWarning();
          return;
        case NEUTRAL:
          increaseNeutral();
          return;
      }

      throw new IllegalArgumentException("invalid severity <" + severity + ">");
    }
  }
}
