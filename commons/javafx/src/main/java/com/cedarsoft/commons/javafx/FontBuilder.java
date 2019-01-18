package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * A builder for {@link Font}s.
 */
public class FontBuilder {
  @Nonnull
  private String family;
  @Nonnull
  private FontWeight weight;
  @Nonnull
  private FontPosture posture;

  private double size;

  public FontBuilder() {
    this(Font.getDefault());
  }

  public FontBuilder(@Nonnull Font font) {
    family = font.getFamily();
    size = font.getSize();
    weight = FontWeight.NORMAL;
    posture = FontPosture.REGULAR;
  }

  @Nonnull
  public static FontBuilder create() {
    return new FontBuilder();
  }

  @Nonnull
  public static FontBuilder createWithFont(@Nonnull Font font) {
    return new FontBuilder(font);
  }

  @Nonnull
  public FontBuilder withFamily(@Nonnull String family) {
    this.family = family;
    return this;
  }

  @Nonnull
  public FontBuilder withWeight(@Nonnull FontWeight weight) {
    this.weight = weight;
    return this;
  }

  @Nonnull
  public FontBuilder withPosture(@Nonnull FontPosture posture) {
    this.posture = posture;
    return this;
  }

  @Nonnull
  public FontBuilder withSize(double size) {
    this.size = size;
    return this;
  }

  @Nonnull
  public Font build() {
    return Font.font(family, weight, posture, size);
  }
}
