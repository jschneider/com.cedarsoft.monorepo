package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * Pans down
 */
public class PanDownAction extends AbstractPanningAction {
  public PanDownAction(@Nonnull TilesComponent tilesComponent, @Nonnull PanningStep panningStep) {
    super("panLeft", tilesComponent, panningStep);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    tilesComponent.moveView(0, panningStep.getSize());
  }
}
