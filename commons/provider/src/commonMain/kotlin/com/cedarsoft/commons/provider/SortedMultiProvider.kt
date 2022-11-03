package com.cedarsoft.commons.provider

/**
 * Sorted multi provider - requires an index mapping form a [SortedSizedProvider].
 *
 * Create instances by calling [SortedSizedProvider.wrapMultiProvider]
 */
class SortedMultiProvider<in IndexContext, out T>(
  val delegate: MultiProvider<IndexContext, T>,
  /**
   * The index mapping that is used to map the index before using the [delegate]
   */
  val indexMapping: IndexMapping,
) : MultiProvider<IndexContext, T> {
  override fun valueAt(index: Int): T {
    val mappedIndex = indexMapping.mappedIndex(index)
    return delegate.valueAt(mappedIndex)
  }
}
