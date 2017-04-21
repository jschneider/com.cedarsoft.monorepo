package com.cedarsoft.tiles.feature;

import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.feature.action.AbstractTilesComponentAction;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * Sets the default zoom factor
 */
public class DefaultZoomAction extends AbstractTilesComponentAction {

  public DefaultZoomAction(@Nonnull TilesComponent tilesComponent) {
    super("defaultZoom", tilesComponent);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    tilesComponent.resetZoom();
  }
}
