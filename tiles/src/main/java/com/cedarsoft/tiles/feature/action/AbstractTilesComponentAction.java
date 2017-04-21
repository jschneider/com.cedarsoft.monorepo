package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;
import javax.swing.AbstractAction;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractTilesComponentAction extends AbstractAction {
  @Nonnull
  protected final TilesComponent tilesComponent;

  protected AbstractTilesComponentAction(@Nonnull String name, @Nonnull TilesComponent tilesComponent) {
    super(name);
    this.tilesComponent = tilesComponent;
  }
}
