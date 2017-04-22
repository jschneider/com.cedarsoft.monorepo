package com.cedarsoft.swing.components.timeline;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
