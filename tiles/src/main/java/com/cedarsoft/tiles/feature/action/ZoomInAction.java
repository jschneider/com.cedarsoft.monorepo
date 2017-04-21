package com.cedarsoft.tiles.feature.action;

import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.ZoomModifier;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

/**
 * Zooms in
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ZoomInAction extends AbstractZoomAction {

  public ZoomInAction(@Nonnull TilesComponent tilesComponent, @Nonnull ZoomModifier zoomModifier) {
    super("zoomIn", tilesComponent, zoomModifier);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final boolean controlPressed = (e.getModifiers() & ActionEvent.CTRL_MASK) != 0;
    final boolean shiftPressed = (e.getModifiers() & ActionEvent.SHIFT_MASK) != 0;

    tilesComponent.zoomView(zoomModifier.getZoomX(-1, controlPressed, shiftPressed),
                            zoomModifier.getZoomY(-1, controlPressed, shiftPressed));
  }
}
