package com.cedarsoft.formatting

import com.cedarsoft.charting.annotations.JavaFriendly
import com.cedarsoft.i18n.I18nConfiguration

/**
 * Formats a value adding an offset to it
 */
class OffsetFormatter(
  /**
   * The delegate that is used to format the offset value
   */
  val delegate: CachedFormatter,
  /**
   * Provides the offset that is added to the value before formatting using the delegate
   */
  val offsetProvider: () -> Double
) : CachedFormatter {
  @JavaFriendly
  constructor(
    delegate: CachedFormatter,
    offsetProvider: OffsetProvider
  ) : this(delegate, offsetProvider::offset)

  override val currentCacheSize: Int
    get() = delegate.currentCacheSize

  override fun format(value: Double, i18nConfiguration: I18nConfiguration): String {
    val offset = offsetProvider()
    return delegate.format(value + offset, i18nConfiguration)
  }

  override val precision: Double
    get() = delegate.precision
}

/**
 * Provides the offset
 */
@JavaFriendly
interface OffsetProvider {
  /**
   * Returns the offset
   */
  val offset: Double
}
