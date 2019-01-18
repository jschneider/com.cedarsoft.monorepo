package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.Node;

/**
 * Helper class with styles
 */
public class StyleHelper {
  private StyleHelper() {
  }

  @Nonnull
  public static <T extends Node> T withStyleClass(@Nonnull T node, @Nonnull String styleClass) {
    node.getStyleClass().add(styleClass);
    return node;
  }
}
