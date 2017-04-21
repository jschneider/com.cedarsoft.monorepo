package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * Pans up
 */
public class PanUpAction extends AbstractPanningAction {

  public PanUpAction(@Nonnull TilesComponent tilesComponent, @Nonnull PanningStep panningStep) {
    super("panUp", tilesComponent, panningStep);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    tilesComponent.moveView(0, -panningStep.getSize());
  }
}
