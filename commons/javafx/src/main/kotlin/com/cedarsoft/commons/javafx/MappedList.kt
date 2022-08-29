package com.cedarsoft.commons.javafx

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.TransformationList
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Maps elements of type IN to OUT with change listeners working as expected.
 */
class MappedList<IN, OUT>(
  source: ObservableList<out IN>,
  mapper: Function<IN, OUT>,
) : TransformationList<OUT, IN>(source) {
  private val mapper: Function<IN, OUT>
  private val mapped: ArrayList<OUT>

  /**
   * Creates a new MappedList list wrapped around the source list.
   * Each element will have the given function applied to it, such that the list is cast through the mapper.
   */
  init {
    this.mapper = mapper
    mapped = ArrayList(source.size)
    mapAll()
  }

  private fun mapAll() {
    mapped.clear()
    for (currentValue in source) {
      mapped.add(mapper.apply(currentValue))
    }
  }

  override fun sourceChanged(c: ListChangeListener.Change<out IN>) {
    // Is all this stuff right for every case? Probably it doesn't matter for this app.
    beginChange()
    while (c.next()) {
      if (c.wasPermutated()) {
        val perm = IntArray(c.to - c.from)
        for (i in c.from until c.to) {
          perm[i - c.from] = c.getPermutation(i)
        }
        nextPermutation(c.from, c.to, perm)
      } else if (c.wasUpdated()) {
        for (i in c.from until c.to) {
          remapIndex(i)
          nextUpdate(i)
        }
      } else {
        if (c.wasRemoved()) {
          // Removed should come first to properly handle replacements, then add.
          val removed = mapped.subList(c.from, c.from + c.removedSize)
          val duped: List<OUT> = ArrayList(removed)
          removed.clear()
          nextRemove(c.from, duped)
        }
        if (c.wasAdded()) {
          for (i in c.from until c.to) {
            mapped.addAll(c.from, c.addedSubList.stream().map(mapper).collect(Collectors.toList()))
            remapIndex(i)
          }
          nextAdd(c.from, c.to)
        }
      }
    }
    endChange()
  }

  private fun remapIndex(i: Int) {
    if (i >= mapped.size) {
      for (j in mapped.size..i) {
        mapped.add(mapper.apply(source[j]))
      }
    }
    mapped[i] = mapper.apply(source[i])
  }

  override fun getSourceIndex(index: Int): Int {
    return index
  }

  /**
   * Do not @Override annotation since this method has only been added in Java 9
   */
  fun getViewIndex(index: Int): Int {
    return index
  }

  override fun get(index: Int): OUT {
    return mapped[index]
  }

  override val size: Int
    get() {
      return mapped.size
    }
}
