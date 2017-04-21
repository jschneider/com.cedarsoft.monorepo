package com.cedarsoft.tiles.feature;

import com.cedarsoft.tiles.ActivatableOverlay;
import com.cedarsoft.tiles.StatsOverlay;
import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * This feature supports stats that can be activated
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ActivatableStatsFeature {
  @Nonnull
  private static final Object ACTION_KEY_TOGGLE_STATS = "toggleStatsOverlay";
  @Nonnull
  private final ActivatableOverlay activatableOverlay;
  @Nonnull
  private final TilesComponent tilesComponent;

  public ActivatableStatsFeature(@Nonnull TilesComponent tilesComponent) {
    this.tilesComponent = tilesComponent;
    StatsOverlay statsOverlay = new StatsOverlay();
    activatableOverlay = new ActivatableOverlay(statsOverlay);

    //disable per default
    activatableOverlay.setActive(false);
    tilesComponent.addOverlay(activatableOverlay);

    fillMaps();
  }

  private void fillMaps() {
    ActionMap actionMap = tilesComponent.getActionMap();
    actionMap.put(ACTION_KEY_TOGGLE_STATS, new ToggleActivatableOverlayAction("toggleStatsOverlay", tilesComponent, activatableOverlay));

    InputMap inputMap = tilesComponent.getInputMap();
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_TOGGLE_STATS);
  }

  @Nonnull
  public static ActivatableStatsFeature install(@Nonnull TilesComponent tilesComponent) {
    return new ActivatableStatsFeature(tilesComponent);
  }

}
