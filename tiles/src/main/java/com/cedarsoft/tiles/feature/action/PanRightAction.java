package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * Pans right
 */
public class PanRightAction extends AbstractPanningAction {
  public PanRightAction(@Nonnull TilesComponent tilesComponent, @Nonnull PanningStep panningStep) {
    super("panRight", tilesComponent, panningStep);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    tilesComponent.moveView(panningStep.getSize(), 0);
  }
}
