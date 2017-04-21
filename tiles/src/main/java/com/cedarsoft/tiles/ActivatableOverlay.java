package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An overlay that may be activated/deactivated
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ActivatableOverlay implements Overlay {
  @Nonnull
  private final Overlay delegate;
  @Nonnull
  private final AtomicBoolean activated = new AtomicBoolean(true);

  public ActivatableOverlay(@Nonnull Overlay delegate) {
    this.delegate = delegate;
  }

  public void setActive(boolean active) {
    activated.set(active);
  }

  @UiThread
  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    if (!activated.get()) {
      return;
    }

    delegate.paint(g2d, tilesComponent);
  }

  public void toggle() {
    if (activated.weakCompareAndSet(true, false)) {
      //If could be toggled, return
      return;
    }

    //Else try the other way
    activated.weakCompareAndSet(false, true);
  }
}
