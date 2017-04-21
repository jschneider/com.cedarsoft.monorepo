package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;

import javax.annotation.Nonnull;

/**
 * Abstract base class for panning related actions
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractPanningAction extends AbstractTilesComponentAction {
  @Nonnull
  protected final PanningStep panningStep;

  protected AbstractPanningAction(@Nonnull String name, @Nonnull TilesComponent tilesComponent, @Nonnull PanningStep panningStep) {
    super(name, tilesComponent);
    this.panningStep = panningStep;
  }
}
