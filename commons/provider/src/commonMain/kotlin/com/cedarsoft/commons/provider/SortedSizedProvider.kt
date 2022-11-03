package com.cedarsoft.commons.provider

import com.cedarsoft.common.kotlin.lang.DoublesComparator

/**
 * Sorts the values
 */
class SortedSizedProvider<T>(
  /**
   * The delegating sized provider
   */
  val delegate: SizedProvider<T>,

  /**
   * The comparator that is used to sort the elements.
   */
  val comparator: Comparator<T>,
) : SizedProvider<T>, IndexMapping {

  private val indexMappingSupport: IndexMappingSupport = IndexMappingSupport { indexA, indexB ->
    val valueA = delegate.valueAt(indexA)
    val valueB = delegate.valueAt(indexB)

    comparator.compare(valueA, valueB)
  }

  override fun size(): Int {
    return updateIndexMap()
  }

  /**
   * Updates the index map
   * @return the size!
   */
  fun updateIndexMap(): Int {
    return delegate.size().also {
      indexMappingSupport.updateMapping(it)
    }
  }

  override fun valueAt(index: Int): T {
    val mappedIndex = mappedIndex(index)
    return delegate.valueAt(mappedIndex)
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
  fun <IndexContext, T> wrapMultiProvider(delegate: MultiProvider<IndexContext, T>): SortedMultiProvider<IndexContext, T> {
    return SortedMultiProvider(delegate, this)
  }
}

/**
 * Wraps this sorted provider within a sorted doubles provider
 */
fun <T> SizedProvider<T>.sorted(comparator: Comparator<T>): SortedSizedProvider<T> {
  return SortedSizedProvider(this, comparator)
}

/**
 * Avoid accidental sorting of already sorted providers
 */
@Deprecated("Do not sort again!", level = DeprecationLevel.ERROR)
fun <T> SortedSizedProvider<T>.sorted(comparator: Comparator<T>): SortedSizedProvider<T> {
  TODO()
}
