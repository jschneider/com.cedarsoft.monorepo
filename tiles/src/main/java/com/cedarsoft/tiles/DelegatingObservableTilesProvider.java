package com.cedarsoft.tiles;

import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;

/**
 * Abstract base class for a delegating tiles updatable
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class DelegatingObservableTilesProvider extends AbstractObservableTilesProvider {
  @Nonnull
  private final TilesProvider delegate;

  protected DelegatingObservableTilesProvider(@Nonnull TilesProvider delegate) {
    this.delegate = delegate;

    if (delegate instanceof ObservableTilesProvider) {
      ((ObservableTilesProvider) delegate).addListener(new Listener() {
        @Override
        public void tileUpdated(@Nonnull ObservableTilesProvider source, @Model double modelXMin, @Model double modelXMax) {
          notifyUpdated(modelXMin, modelXMax);
        }
      });
    }
  }

  @Override
  @px
  @NonBlocking
  public int getWidth() {
    return delegate.getWidth();
  }

  @Override
  @px
  @NonBlocking
  public int getHeight() {
    return delegate.getHeight();
  }

  @Override
  @Nonnull
  public Tile getTile(@Nonnull TileLocation location) {
    return delegate.getTile(location);
  }

  @Override
  @UiThread
  @NonUiThread
  @NonBlocking
  @Nonnull
  public Tile getPlaceholderTile() {
    return delegate.getPlaceholderTile();
  }

  @Nonnull
  public TilesProvider getDelegate() {
    return delegate;
  }
}
