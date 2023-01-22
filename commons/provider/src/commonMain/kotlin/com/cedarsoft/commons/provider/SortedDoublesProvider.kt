package com.cedarsoft.commons.provider

import com.cedarsoft.annotations.CreatesObjects
import com.cedarsoft.common.kotlin.lang.DoublesComparator
import com.cedarsoft.commons.provider.impl.SortedIndexMappingSupport

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

  private val sortedIndexMappingSupport: SortedIndexMappingSupport = SortedIndexMappingSupport { indexA, indexB ->
    val valueA = delegate.valueAt(indexA)
    val valueB = delegate.valueAt(indexB)

    comparator.compare(valueA, valueB)
  }

  override fun mapped2Original(mappedIndex: Int): Int {
    return sortedIndexMappingSupport.mapped2Original(mappedIndex)
  }

  override fun size(): Int {
    return delegate.size().also {
      sortedIndexMappingSupport.updateMapping(it)
    }
  }

  override fun valueAt(index: Int): Double {
    val originalIndex = mapped2Original(index)
    return delegate.valueAt(originalIndex)
  }

  /**
   * Wraps the provided delegate and returns the values matching to the current sorted values
   */
  fun <IndexContextOld, IndexContextNew, T> wrapMultiProvider(delegate: MultiProvider<IndexContextOld, T>): MappedIndexMultiProvider<IndexContextNew, T> {
    return MappedIndexMultiProvider(delegate, this)
  }
}

/**
 * Wraps this sorted provider within a sorted [DoublesProvider].
 *
 * Attention: A new object is created!
 */
@CreatesObjects
fun DoublesProvider.sorted(comparator: DoublesComparator = DoublesComparator.natural): SortedDoublesProvider {
  return SortedDoublesProvider(this, comparator)
}

/**
 * Avoid accidental sorting of already sorted providers
 */
@Deprecated("Do not sort again!", level = DeprecationLevel.ERROR)
fun SortedDoublesProvider.sorted(comparator: DoublesComparator = DoublesComparator.natural): SortedDoublesProvider {
  throw UnsupportedOperationException("Must not be used")
}
