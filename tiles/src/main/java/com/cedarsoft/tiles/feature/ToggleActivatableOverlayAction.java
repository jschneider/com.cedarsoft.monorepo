package com.cedarsoft.tiles.feature;

import com.cedarsoft.tiles.ActivatableOverlay;
import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.feature.action.AbstractTilesComponentAction;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * This action toggles the state of an activatable overlay
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ToggleActivatableOverlayAction extends AbstractTilesComponentAction {
  @Nonnull
  private final ActivatableOverlay activatableOverlay;

  public ToggleActivatableOverlayAction(@Nonnull String name, @Nonnull TilesComponent tilesComponent, @Nonnull ActivatableOverlay activatableOverlay) {
    super(name, tilesComponent);
    this.activatableOverlay = activatableOverlay;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    activatableOverlay.toggle();
    tilesComponent.scheduleRepaint("asdf");
  }
}
