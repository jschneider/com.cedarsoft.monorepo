package com.cedarsoft.swing.components.timeline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;

/**
 * A timeline
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Timeline extends JComponent {
  public static final Color COLOR_VISIBLE_AREA = new Color(255, 251, 13, 50);
  public static final Color COLOR_VISIBLE_AREA_BORDER = new Color(255, 169, 70, 50);
  public static final Color LOCATION_MARKER_COLOR = new Color(175, 152, 255);

  private static final Color WARNING_COLOR = new Color(248, 210, 128);
  private static final Color ERROR_COLOR = new Color(229, 103, 107);

  @px
  public static final int NET_HEIGHT = 100;
  @px
  public static final int AXIS_HEIGHT = 16;
  @px
  public static final int BAR_WIDTH = 4;
  @px
  public static final int BAR_GAP = 3;
  @px
  public static final int PADDING_LEFT = 5;
  @px
  public static final int PADDING_RIGHT = 5;

  @Nonnull
  private TimelineModel model = new NullTimelineModel();
  @Nonnull
  private NavigationHandler navigationHandler = new NullNavigationHandler();
  @Nonnull
  private final TimelineModel.Listener modelListener = new TimelineModel.Listener() {
    @Override
    @UiThread
    public void modelChanged(@Nonnull TimelineModel model) {
      invalidate();
      repaint();
    }

    @Override
    @UiThread
    public void visibleAreaUpdated(@Nonnull TimelineModel model, @Nullable TimelineModel.VisibleArea visibleArea) {
      if (visibleArea != null) {
        Rectangle2D.Double visibleAreaRect = getVisibleAreaRect(visibleArea);
        scrollRectToVisible(new Rectangle((int) visibleAreaRect.x - 5, (int) visibleAreaRect.y, (int) visibleAreaRect.width + 10,
                                          (int) visibleAreaRect.height));
      }
      repaint();
    }
  };

  public Timeline(@Nonnull TimelineModel model, @Nonnull final NavigationHandler navigationHandler) {
    this();
    this.navigationHandler = navigationHandler;
    setModel(model);
  }

  public Timeline() {
    setBackground(Color.WHITE);
    setOpaque(true);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        double x = e.getPoint().getX();
        int dayIndex = px2dayIndex(x - caluclateXCorrection());

        navigationHandler.navigateTo(dayIndex);
      }
    });
  }

  public final void setModel(@Nonnull TimelineModel model) {
    this.model.removeListener(modelListener);
    this.model = model;
    this.model.addListener(modelListener);
  }

  public void setNavigationHandler(@Nonnull NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  private static int px2dayIndex(@px double x) {
    @px double netX = x - PADDING_LEFT;
    double index = netX / (BAR_WIDTH + BAR_GAP);

    return (int) index;
  }

  @px
  private static double dayIndex2px(int index) {
    return index * (BAR_WIDTH + BAR_GAP) + PADDING_LEFT;
  }

  @Override
  public Dimension getPreferredSize() {
    @px int height = NET_HEIGHT + AXIS_HEIGHT + 10;
    if (!model.isValid()) {
      Dimension preferredSize = super.getPreferredSize();
      preferredSize.height = height;
      return preferredSize;
    }

    @px int width = model.getBarsCount() * (BAR_WIDTH + BAR_GAP) - BAR_GAP + PADDING_LEFT + PADDING_RIGHT;
    return new Dimension(width, height);
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(super.getMinimumSize().width, AXIS_HEIGHT + 10 + 30);
  }

  @Override
  public Dimension getMaximumSize() {
    return new Dimension(super.getMaximumSize().width, Integer.MAX_VALUE);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D graphics = (Graphics2D) g;
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    if (!model.isValid()) {
      @Nullable Color bg = UIManager.getColor("TextField.disabledBackground");
      if (bg != null) {
        graphics.setColor(bg);
      }
      else {
        graphics.setColor(Color.LIGHT_GRAY);
      }
      graphics.fillRect(0, 0, getWidth(), getHeight());

      String message = "Inconsistent data"; //TODO extract
      Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(message, graphics);
      graphics.setColor(Color.BLACK);
      graphics.drawString(message, (float) (getWidth() / 2.0 - bounds.getWidth() / 2.0), (float) (getHeight() / 2.0 - bounds.getHeight() / 2.0));
      return;
    }

    graphics.setColor(getBackground());
    graphics.fillRect(0, 0, getWidth(), getHeight());

    int moveToRight = caluclateXCorrection();

    {
      Graphics2D copy = (Graphics2D) graphics.create();
      try {
        copy.translate(moveToRight, 0);
        drawVisibleArea(copy);
      } finally {
        copy.dispose();
      }
    }

    {
      Graphics2D copy = (Graphics2D) graphics.create();
      try {
        drawAxis(copy);
      } finally {
        copy.dispose();
      }
    }

    {
      Graphics2D copy = (Graphics2D) graphics.create();
      try {
        copy.translate(moveToRight, 0);
        drawAxisLabels(copy);
      } finally {
        copy.dispose();
      }
    }

    {
      Graphics2D copy = (Graphics2D) graphics.create();
      try {
        copy.translate(moveToRight, 0);
        drawSegments(copy);
      } finally {
        copy.dispose();
      }
    }
  }

  @px
  private int caluclateXCorrection() {
    @px int totalWidth = model.getBarsCount() * (BAR_WIDTH + BAR_GAP) - BAR_GAP;

    if (getWidth() > totalWidth + PADDING_LEFT + PADDING_RIGHT) {
      return getWidth() - totalWidth - PADDING_LEFT - PADDING_RIGHT;
    }

    return 0;
  }

  private void drawAxis(@Nonnull Graphics2D g2d) {
    g2d.translate(PADDING_LEFT, NET_HEIGHT + 5);

    g2d.setColor(Color.BLACK);
    g2d.draw(new Line2D.Double(0, 0, getWidth() - PADDING_LEFT - PADDING_RIGHT, 0));
  }

  private void drawAxisLabels(@Nonnull Graphics2D g2d) {
    g2d.translate(PADDING_LEFT, NET_HEIGHT + 5);

    g2d.setColor(Color.BLACK);

    int days = model.getBarsCount();
    for (int i = 0; i < days; i++) {
      double toCenter = BAR_WIDTH / 2.0;

      g2d.translate(toCenter, 0);
      if ((i + 4) % 8 == 0) {
        g2d.drawLine(0, -1, 0, 2);

        int height = g2d.getFontMetrics().getHeight();
        String date = model.getLabel(i);
        Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(date, g2d);

        g2d.drawString(date, (float) (-bounds.getWidth() / 2.0), height);
      }

      g2d.translate(BAR_WIDTH + BAR_GAP - toCenter, 0);
    }
  }

  private void drawSegments(@Nonnull Graphics2D g2d) {
    g2d.translate(PADDING_LEFT, NET_HEIGHT);

    int days = model.getBarsCount();
    for (int i = 0; i < days; i++) {

      TimelineModel.BarValues barValues = model.getBarValues(i);

      double base = 0;

      drawPart(g2d, base, barValues.getValueCritical(), ERROR_COLOR);

      base += barValues.getValueCritical();
      drawPart(g2d, base, barValues.getValueWarnings(), WARNING_COLOR);

      base += barValues.getValueWarnings();
      drawPart(g2d, base, barValues.getValueNeutral(), Color.GRAY);

      g2d.translate(BAR_WIDTH + BAR_GAP, 0);
    }
  }

  private void drawVisibleArea(@Nonnull Graphics2D g2d) {
    g2d.translate(0, 0);

    @Nullable
    TimelineModel.VisibleArea visibleArea = model.getVisibleArea();
    if (visibleArea == null) {
      return;
    }

    g2d.setColor(COLOR_VISIBLE_AREA);
    Rectangle2D.Double visibleAreaRect = getVisibleAreaRect(visibleArea);
    g2d.fill(visibleAreaRect);
    g2d.setColor(COLOR_VISIBLE_AREA_BORDER);
    g2d.draw(visibleAreaRect);
  }

  @px
  @Nonnull
  private Rectangle2D.Double getVisibleAreaRect(@Nonnull TimelineModel.VisibleArea visibleArea) {
    @px double begin = dayIndex2px(Math.max(0, visibleArea.getLower()));
    @px double end = dayIndex2px(Math.min(model.getBarsCount(), visibleArea.getUpper())) + BAR_WIDTH;
    return new Rectangle2D.Double(begin - 1, 0, end - begin + 1 + 2, getHeight() - 1);
  }

  private static void drawPart(@Nonnull Graphics2D g2d, double base, double height, @Nonnull Color color) {
    Rectangle2D.Double rect = new Rectangle2D.Double(0, -height - base, BAR_WIDTH, height);
    g2d.setColor(color);
    g2d.fill(rect);
    g2d.setColor(Color.DARK_GRAY);
    g2d.draw(rect);
  }

  public interface NavigationHandler {
    void navigateTo(int index);
  }

  private static class NullNavigationHandler implements NavigationHandler {
    @Override
    public void navigateTo(int index) {
    }
  }

  private static class NullTimelineModel extends AbstractTimelineModel {
    private NullTimelineModel() {
    }

    @Override
    public boolean isValid() {
      return true;
    }
  }
}
