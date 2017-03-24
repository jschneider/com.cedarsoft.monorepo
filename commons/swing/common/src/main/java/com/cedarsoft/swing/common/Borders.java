package com.cedarsoft.swing.common;

import javax.annotation.Nonnull;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

/**
 * Contains commonly used borders
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Borders {
  public static final int FIVE = UiScaler.scale(5);
  public static final int TEN = UiScaler.scale(10);
  public static final int TWO = UiScaler.scale(2);

  @Nonnull
  public static final Border DIALOG_CONTENT_BORDER = BorderFactory.createCompoundBorder(
    BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)
    , BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN)
  );

  @Nonnull
  public static final Border DIALOG_BUTTON_PANEL_BORDER = BorderFactory.createEmptyBorder(TEN, UiScaler.scale(100), TEN, TEN);
  @Nonnull
  public static final Border DEFAULT_10_PX = BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN);
  @Nonnull
  public static final Border DEFAULT_5_PX = BorderFactory.createEmptyBorder(FIVE, FIVE, FIVE, FIVE);

  @Nonnull
  public static final Border TOP_2_PX = BorderFactory.createEmptyBorder(TWO, 0, 0, 0);
  @Nonnull
  public static final Border TOP_5_PX = BorderFactory.createEmptyBorder(FIVE, 0, 0, 0);
  @Nonnull
  public static final Border TOP_10_PX = BorderFactory.createEmptyBorder(TEN, 0, 0, 0);

  @Nonnull
  public static final Border ETCHED = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

  @Nonnull
  public static final Border MESSAGE_BORDER = BorderFactory.createCompoundBorder(
    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY)
    , BorderFactory.createEmptyBorder(FIVE, FIVE, FIVE, FIVE)
  );

  @Deprecated
  @Nonnull
  public static final Border EMPTY_5_PX = DEFAULT_5_PX;


  private Borders() {
  }

}
