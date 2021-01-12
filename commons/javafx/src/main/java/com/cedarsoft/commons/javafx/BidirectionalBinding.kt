package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.properties.*
import javafx.beans.property.BooleanProperty
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javax.annotation.Nonnull

/**
 * Helper methods for bidirectional bindings
 */
object BidirectionalBinding {
  /**
   * Registers the given change listeners to the corresponding property.
   * Avoids reoccurring events by wrapping the change listener.
   *
   *
   * <u>Beware that the order matters:</u><br></br>
   * this method copies the value from propertyA to propertyB initially by calling updateB!
   *
   * @param updateFromAToB is called when A has changed and should update property B
   * @param updateFromBToA is called when B has changed and should update property A
   */
  @JvmStatic
  fun <A, B> bindBidirectional(
    @Nonnull propertyA: ObservableValue<A>,
    @Nonnull propertyB: ObservableValue<B>,
    @Nonnull updateFromAToB: ChangeListener<A>,
    @Nonnull updateFromBToA: ChangeListener<B>,
    vararg additionalDependencies: ObservableValue<out Any>
  ) {
    propertyA.addListener(FlaggedChangeListener(updateFromAToB))
    propertyB.addListener(FlaggedChangeListener(updateFromBToA))

    for (additionalDependency in additionalDependencies) {
      additionalDependency.addListener(FlaggedChangeListener { _: ObservableValue<*>?, _: Any?, _: Any? ->
        updateFromAToB.changed(propertyA, null, propertyA.value)
        updateFromBToA.changed(propertyB, null, propertyB.value)
      })
    }

    //Initially update property B
    updateFromAToB.changed(propertyA, null, propertyA.value)
  }

  /**
   * Binds a boolean property and an enum property
   */
  @JvmStatic
  fun <E : Enum<E>> bindBidirectional(
    @Nonnull propertyA: BooleanProperty,
    @Nonnull propertyB: Property<E>,
    /**
     * The enum value that represents true
     */
    trueValue: E,
    /**
     * The enum value that represents false
     */
    falseValue: E,
    vararg additionalDependencies: ObservableValue<out Any>
  ) {

    bindBidirectional(propertyA, propertyB,
                      ChangeListener { _, _, newValue ->
                        propertyB.value = if (newValue) trueValue else falseValue
                      }, { _, _, newValue ->
                        propertyA.value = newValue == trueValue
                      }, *additionalDependencies
    )
  }

  /**
   * Binds properties including a writable state
   */
  @JvmStatic
  fun <A, B> bindBidirectional(
    @Nonnull propertyA: PropertyWithWritableState<A>,
    @Nonnull propertyB: PropertyWithWritableState<B>,
    @Nonnull updateFromAToB: ChangeListener<A>,
    @Nonnull updateFromBToA: ChangeListener<B>,
    vararg additionalDependencies: ObservableValue<out Any>
  ) {
    bindBidirectional(propertyA.property, propertyB.property, updateFromAToB, updateFromBToA, *additionalDependencies)

    //Bind the writable state bidirectional
    propertyB.writablePropertyWritable.bind(propertyA.writableProperty)
  }

  /**
   * Flagged change listener that avoids cycles
   */
  internal class FlaggedChangeListener<T> internal constructor(
    private val updateProperty: ChangeListener<T>
  ) : ChangeListener<T> {
    /**
     * Is set to true if a call is currently made
     */
    private var alreadyCalled = false
    override fun changed(observable: ObservableValue<out T>, oldValue: T, newValue: T) {
      if (alreadyCalled) {
        return
      }
      try {
        alreadyCalled = true
        updateProperty.changed(observable, oldValue, newValue)
      } finally {
        alreadyCalled = false
      }
    }
  }
}
