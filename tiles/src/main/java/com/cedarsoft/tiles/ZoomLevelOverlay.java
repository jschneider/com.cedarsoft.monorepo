package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.swing.common.ColorTools;
import com.cedarsoft.swing.common.SwingHelper;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.NumberFormat;

/**
 * Overlay that paints the zoom level
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ZoomLevelOverlay implements Overlay {
  @Nonnull
  public static final Color BG_COLOR = ColorTools.withAlpha(Color.WHITE, 0.7);
  @Nonnull
  public static final Color SOLID_COLOR = Color.LIGHT_GRAY;
  @px
  public static final int BOUNDS_SIZE = 15;
  @px
  public static final int DIAMETER = 35;
  @px
  public static final int HANDLE_LENGTH = 10;

  @Override
  @UiThread
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    SwingHelper.enableAntialiasing(g2d);

    int zoomLevelX = tilesComponent.getZoomLevelX();
    int zoomLevelY = tilesComponent.getZoomLevelY();

    @VisibleArea @px int baseX = tilesComponent.getWidth() - BOUNDS_SIZE - DIAMETER;

    g2d.setColor(BG_COLOR);
    g2d.fill(new Ellipse2D.Double(baseX, BOUNDS_SIZE, DIAMETER, DIAMETER));
    g2d.setStroke(new BasicStroke(3));
    g2d.setColor(SOLID_COLOR);
    g2d.draw(new Ellipse2D.Double(baseX, BOUNDS_SIZE, DIAMETER, DIAMETER));

    g2d.setStroke(new BasicStroke(5));
    g2d.draw(new Line2D.Double(baseX - HANDLE_LENGTH, BOUNDS_SIZE + DIAMETER + HANDLE_LENGTH, baseX + 4, BOUNDS_SIZE + DIAMETER - 4));

    drawInnerString(g2d, "x: " + zoomLevelX, baseX, DIAMETER, 15 + BOUNDS_SIZE);
    drawInnerString(g2d, "y: " + zoomLevelY, baseX, DIAMETER, 26 + BOUNDS_SIZE);

    //Draw the sub sampling rate
    drawInnerString(g2d, getSubSamplingRangeLabel(tilesComponent.getZoomFactorX()), baseX, DIAMETER, 26 + BOUNDS_SIZE + 23);
  }

  @Nonnull
  protected String getSubSamplingRangeLabel(double zoomFactorX) {
    return NumberFormat.getInstance().format(zoomFactorX);
  }

  private static void drawInnerString(@Nonnull Graphics2D g2d, @Nonnull String string, int baseX, int diameter, int y) {
    @px int widthX = g2d.getFontMetrics().stringWidth(string);
    g2d.drawString(string, baseX + diameter / 2.0f - widthX / 2.0f, y);
  }
}
