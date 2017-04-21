package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.text.NumberFormat;

/**
 * Overlay that shows debugging info
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DebugOverlay implements Overlay {
  @Nonnull
  private final NumberFormat format;

  public DebugOverlay() {
    format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(1);
    format.setMinimumFractionDigits(1);
  }

  @UiThread
  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    //Debug
    g2d.setColor(Color.ORANGE);
    g2d.drawLine(-10, 0, 10, 0);
    g2d.drawLine(0, -10, 0, 10);
    g2d.draw(new Ellipse2D.Double(-10, -10, 20, 20));

    g2d.setColor(Color.RED);
    g2d.drawLine(-10, 0, 10, 0);
    g2d.drawLine(0, -10, 0, 10);

    @Model Point2D.Double modelValueInOrigin = tilesComponent.getConverter().visibleArea2Model((double) 0, (double) 0);

    Point2D.Double viewMovement = tilesComponent.getViewMovement();
    double zoomFactorX = tilesComponent.getZoomFactorX();
    double zoomFactorY = tilesComponent.getZoomFactorY();

    //The value that is shown in the top left of the component
    @View @px Point2D.Double shownInOrigin = new Point2D.Double(-viewMovement.x, -viewMovement.y);
    String locationString = format.format(shownInOrigin.x) + "/" + format.format(shownInOrigin.y) + " (" + format.format(modelValueInOrigin.x) + "/" + format.format(modelValueInOrigin.y) + ")";
    @px int stringWidth = g2d.getFontMetrics().stringWidth(locationString);

    paintBackground(g2d, stringWidth + 5, 28);

    g2d.setColor(Color.BLACK);
    g2d.drawString(locationString, 4, 12);
    format.setMaximumFractionDigits(4);
    g2d.drawString("Zoom: " + format.format(zoomFactorX) + "/" + format.format(zoomFactorX), 4, 25);
  }

  private static void paintBackground(@Nonnull Graphics2D g2d, int width, int height) {
    g2d.setColor(new Color(255, 255, 255, 200));
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.drawRect(0, 0, width, height);
  }
}
