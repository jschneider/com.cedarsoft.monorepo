package com.cedarsoft.commons.provider

/**
 * Sorts the values
 */
class SortedSizedProvider1<T, P1>(
  /**
   * The delegating sized provider
   */
  val delegate: SizedProvider1<T, P1>,

  /**
   * The comparator that is used to sort the elements.
   */
  val comparator: Comparator<T>,
) : SizedProvider1<T, P1>, IndexMapping {

  private val indexMappingSupport: IndexMappingSupport1<P1> = IndexMappingSupport1 { indexA, indexB, param1: P1 ->
    val valueA = delegate.valueAt(indexA, param1)
    val valueB = delegate.valueAt(indexB, param1)

    comparator.compare(valueA, valueB)
  }

  override fun size(param1: P1): Int {
    return updateIndexMap(param1)
  }

  /**
   * Updates the index map
   * @return the size!
   */
  fun updateIndexMap(param1: P1): Int {
    return delegate.size(param1).also { size ->
      indexMappingSupport.updateMapping(size, param1)
    }
  }

  override fun valueAt(index: Int, param1: P1): T {
    val mappedIndex = mappedIndex(index)
    return delegate.valueAt(mappedIndex, param1)
  }

  /**
   * Returns the mapped index.
   *
   * ATTENTION: It is required to call [size] (or [updateIndexMap] in rare circumstances) to update the index mapping first.
   */
  override fun mappedIndex(originalIndex: Int): Int {
    return indexMappingSupport.mappedIndex(originalIndex)
  }

  /**
   * Wraps the provided delegate and returns the values matching to the current sorted values
   */
  fun <IndexContext, T, P1> wrapMultiProvider(delegate: MultiProvider1<IndexContext, T, P1>): SortedMultiProvider1<IndexContext, T, P1> {
    return SortedMultiProvider1(delegate, this)
  }
}
