package com.cedarsoft.swing.common;

import com.cedarsoft.unit.other.px;
import com.jidesoft.swing.JideSplitPane;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Graphics;

/**
 * Split pane that applies the divider location on the first paint
 */
public class CJideSplitPane extends JideSplitPane {
  public CJideSplitPane(int newOrientation) {
    super(newOrientation);
    setShowGripper(false);
    setContinuousLayout(true);
    setDividerSize(4);
  }

  @Nullable
  private int[] dividerLocationsToApply;

  private boolean hasBeenPainted;

  @Override
  public void setDividerLocations(@px @Nonnull int[] locations) {
    super.setDividerLocations(locations);

    if (!hasBeenPainted) {
      this.dividerLocationsToApply = locations.clone();
    }
  }

  @Override
  public void paint(Graphics g) {
    hasBeenPainted = true;
    if (dividerLocationsToApply != null) {
      setDividerLocations(dividerLocationsToApply);
      dividerLocationsToApply = null;
    }
    super.paint(g);
  }
}
