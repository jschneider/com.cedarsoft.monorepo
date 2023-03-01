package it.neckar.open.javafx.properties

import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

/**
 * Represents a property with a writable state
 */
open class PropertyWithWritableState<T>(
  /**
   * The property itself
   */
  open val property: Property<T>,
) {

  constructor(initialValue: T) : this(SimpleObjectProperty(initialValue))

  /**
   * The value of the property
   */
  var value: T
    get() {
      return property.value
    }
    set(value) {
      property.value = value
    }

  protected val internalWritableProperty = SimpleBooleanProperty()

  /**
   * The writable property for this property
   */
  val writableProperty: ReadOnlyBooleanProperty
    get() {
      return internalWritableProperty
    }

  /**
   * Returns the writable property as writable property.
   * ATTENTION: Use with care!
   */
  val writablePropertyWritable: BooleanProperty
    get() {
      return internalWritableProperty
    }

  /**
   * Whether the property is currently writable
   */
  val writable: Boolean by writableProperty

  fun addListener(listener: ChangeListener<in T>) {
    property.addListener(listener)
  }

  fun bindBidirectional(other: Property<T>, otherWritableState: ObservableValue<out Boolean>) {
    property.bindBidirectional(other)
    internalWritableProperty.bind(otherWritableState)
  }

  fun bindBidirectional(other: PropertyWithWritableState<T>) {
    bindBidirectional(other.property, other.writableProperty)
  }

  companion object {
    @JvmStatic
    fun <T> create(
      property: Property<T>,
      writable: ReadOnlyBooleanProperty
    ): PropertyWithWritableState<T> {
      return PropertyWithWritableState(property).also {
        it.internalWritableProperty.bind(writable)
      }
    }

    @JvmStatic
    fun create(
      property: BooleanProperty,
      writable: ReadOnlyBooleanProperty
    ): BooleanPropertyWithWritableState {
      return BooleanPropertyWithWritableState(property).also {
        it.internalWritableProperty.bind(writable)
      }
    }

    @JvmStatic
    fun create(
      property: IntegerProperty,
      writable: ReadOnlyBooleanProperty
    ): IntegerPropertyWithWritableState {
      return IntegerPropertyWithWritableState(property).also {
        it.internalWritableProperty.bind(writable)
      }
    }

    @JvmStatic
    fun create(
      property: LongProperty,
      writable: ReadOnlyBooleanProperty
    ): LongPropertyWithWritableState {
      return LongPropertyWithWritableState(property).also {
        it.internalWritableProperty.bind(writable)
      }
    }

    @JvmStatic
    fun create(
      property: DoubleProperty,
      writable: ReadOnlyBooleanProperty
    ): DoublePropertyWithWritableState {
      return DoublePropertyWithWritableState(property).also {
        it.internalWritableProperty.bind(writable)
      }
    }
  }
}

class DoublePropertyWithWritableState(
  override val property: DoubleProperty,
) : PropertyWithWritableState<Number>(property) {

  constructor() : this(SimpleDoubleProperty(0.0))
  constructor(initialValue: Double) : this(SimpleDoubleProperty(initialValue))

  fun get(): Double {
    return value.toDouble()
  }

  fun bindBidirectional(other: DoublePropertyWithWritableState) {
    bindBidirectional(other.property, other.writableProperty)
  }
}

class LongPropertyWithWritableState(
  override val property: LongProperty,
) : PropertyWithWritableState<Number>(property) {

  constructor() : this(0L)
  constructor(initialValue: Long) : this(SimpleLongProperty(initialValue))

  fun get(): Long {
    return value.toLong()
  }

  fun bindBidirectional(other: LongPropertyWithWritableState) {
    bindBidirectional(other.property, other.writableProperty)
  }

}

class IntegerPropertyWithWritableState(
  override val property: IntegerProperty,
) : PropertyWithWritableState<Number>(property) {

  constructor(initialValue: Int) : this(SimpleIntegerProperty(initialValue))
  constructor() : this(0)

  fun get(): Int {
    return value.toInt()
  }

  fun bindBidirectional(other: IntegerPropertyWithWritableState) {
    bindBidirectional(other.property, other.writableProperty)
  }

}

class BooleanPropertyWithWritableState(
  override val property: BooleanProperty,
) : PropertyWithWritableState<Boolean>(property) {

  constructor() : this(false)
  constructor(initialValue: Boolean) : this(SimpleBooleanProperty(initialValue))

  fun get(): Boolean {
    return value
  }

  fun bindBidirectional(other: BooleanPropertyWithWritableState) {
    bindBidirectional(other.property, other.writableProperty)
  }
}
