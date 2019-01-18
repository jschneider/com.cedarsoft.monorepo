package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Helper methods for bidirectional bindings
 */
public class BidirectionalBinding {
  private BidirectionalBinding() {
  }

  /**
   * Registers the given change listeners to the corresponding property.
   * Avoids reoccurring events by wrapping the change listener.
   * <p>
   * <u>Beware that the order matters:</u><br>
   * this method copies the value from propertyA to propertyB initially by calling updateB!
   *
   * @param updateFromAtoB is called when A has changed and should update property B
   * @param updateFromBToA is called when B has changed and should update property A
   */
  public static <A, B> void bindBidirectional(@Nonnull ObservableValue<A> propertyA, @Nonnull ObservableValue<B> propertyB, @Nonnull ChangeListener<A> updateFromAtoB, @Nonnull ChangeListener<B> updateFromBToA) {
    propertyA.addListener(new FlaggedChangeListener<>(updateFromAtoB));
    propertyB.addListener(new FlaggedChangeListener<>(updateFromBToA));

    //Initially update property B
    updateFromAtoB.changed(propertyA, null, propertyA.getValue());
  }

  /**
   * Flagged change listener that avoids cycles
   */
  private static class FlaggedChangeListener<T> implements ChangeListener<T> {
    @Nonnull
    private final ChangeListener<T> m_updateProperty;

    /**
     * Is set to true if a call is currently made
     */
    private boolean m_alreadyCalled;

    private FlaggedChangeListener(@Nonnull ChangeListener<T> updateProperty) {
      m_updateProperty = updateProperty;
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
      if (m_alreadyCalled) {
        return;
      }

      try {
        m_alreadyCalled = true;
        m_updateProperty.changed(observable, oldValue, newValue);
      } finally {
        m_alreadyCalled = false;
      }
    }
  }
}
