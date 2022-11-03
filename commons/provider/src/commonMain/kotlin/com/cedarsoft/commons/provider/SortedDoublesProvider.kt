package com.cedarsoft.commons.provider

import com.cedarsoft.common.kotlin.lang.DoublesComparator

/**
 * Sorts the doubles
 */
class SortedDoublesProvider(
  /**
   * The delegate
   */
  val delegate: DoublesProvider,
  /**
   * Is used to compare the doubles
   */
  private val comparator: DoublesComparator = DoublesComparator.natural,
) : DoublesProvider, IndexMapping {

  private val indexMappingSupport: IndexMappingSupport = IndexMappingSupport { indexA, indexB ->
    val valueA = delegate.valueAt(indexA)
    val valueB = delegate.valueAt(indexB)

    comparator.compare(valueA, valueB)
  }

  override fun mappedIndex(originalIndex: Int): Int {
    return indexMappingSupport.mappedIndex(originalIndex)
  }

  override fun size(): Int {
    return delegate.size().also {
      indexMappingSupport.updateMapping(it)
    }
  }

  override fun valueAt(index: Int): Double {
    val mappedIndex = mappedIndex(index)
    return delegate.valueAt(mappedIndex)
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
fun DoublesProvider.sorted(comparator: DoublesComparator = DoublesComparator.natural): SortedDoublesProvider {
  return SortedDoublesProvider(this, comparator)
}

/**
 * Avoid accidental sorting of already sorted providers
 */
@Deprecated("Do not sort again!", level = DeprecationLevel.ERROR)
fun SortedDoublesProvider.sorted(comparator: DoublesComparator = DoublesComparator.natural): SortedDoublesProvider {
  TODO()
}
