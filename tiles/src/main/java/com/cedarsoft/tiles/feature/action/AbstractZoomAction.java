package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.ZoomModifier;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractZoomAction extends AbstractTilesComponentAction {


  @Nonnull
  protected final ZoomModifier zoomModifier;

  public AbstractZoomAction(String name, TilesComponent tilesComponent, @Nonnull ZoomModifier zoomModifier) {
    super(name, tilesComponent);
    this.zoomModifier = zoomModifier;
  }
}
