package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * Pans left
 */
public class PanLeftAction extends AbstractPanningAction {

  public PanLeftAction( @Nonnull TilesComponent tilesComponent, @Nonnull PanningStep panningStep) {
    super("panLeft", tilesComponent, panningStep);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    tilesComponent.moveView(-panningStep.getSize(), 0);
  }
}
