package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Helper class that creates arrows
 */
public class Arrows {
  private static final double ARROW_WIDTH = 5.0;
  private static final double ARROW_WIDTH_HALF = ARROW_WIDTH / 2.0;
  private static final double ARROW_LENGTH = 5.0;

  private Arrows() {
  }

  @Nonnull
  public static Path createArrowToTop(int lineLength) {
    Path arrowHeadPath = new Path();
    arrowHeadPath.getStyleClass().add("arrow");
    arrowHeadPath.getElements().add(new MoveTo(0, -ARROW_LENGTH)); //start @ top of arrow
    arrowHeadPath.getElements().add(new LineTo(ARROW_WIDTH_HALF, 0)); //bottom right
    arrowHeadPath.getElements().add(new LineTo(-ARROW_WIDTH_HALF, 0)); //bottom left
    arrowHeadPath.getElements().add(new LineTo(0, -ARROW_LENGTH)); //back to top of arrow
    arrowHeadPath.getElements().add(new MoveTo(0, 0)); //middle bottom
    arrowHeadPath.getElements().add(new LineTo(0, lineLength)); //middle bottom

    return arrowHeadPath;
  }
}
