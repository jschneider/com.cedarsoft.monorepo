package com.cedarsoft.tiles;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractObservableTilesProvider implements ObservableTilesProvider {
  @Nonnull
  private final List<Listener> listeners = new CopyOnWriteArrayList<>();

  /**
   * Notify the listeners that some tiles have been updated
   *
   * @param modelXMin the x min value that has been changed
   * @param modelXMax the x max value that has been changed
   */
  @NonUiThread
  @UiThread
  protected void notifyUpdated(@Model double modelXMin, @Model double modelXMax) {
    for (Listener listener : listeners) {
      listener.tileUpdated(this, modelXMin, modelXMax);
    }
  }

  @Override
  public void addListener(@Nonnull Listener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeListener(@Nonnull Listener listener) {
    this.listeners.remove(listener);
  }
}
