package com.cedarsoft.commons.javafx

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.ArrayDeque
import java.util.Queue

/**
 */
class MappedListTest {
  private lateinit var inputs: ObservableList<String>
  private lateinit var outputs: ObservableList<String>
  private lateinit var changes: Queue<ListChangeListener.Change<out String>>

  @Before
  fun setup() {
    inputs = FXCollections.observableArrayList()
    outputs = MappedList(inputs) { str: String? -> "Hello $str" }
    changes = ArrayDeque()
    outputs.addListener(ListChangeListener<String> { changes.add(it) })
  }

  @Test
  fun add() {
    assertEquals(0, outputs.size)
    inputs.add("Mike")

    val change = change
    assertTrue(change.wasAdded())
    assertEquals("Hello Mike", change.addedSubList[0])
    assertEquals(1, outputs.size)
    assertEquals("Hello Mike", outputs[0])
    inputs.removeAt(0)
    assertEquals(0, outputs.size)
  }

  private val change: ListChangeListener.Change<out String?>
    get() {
      val change = changes.poll()
      change.next()
      return change
    }

  @Test
  fun remove() {
    inputs.add("Mike")
    inputs.add("Dave")
    inputs.add("Katniss")
    change
    change
    change
    assertEquals("Hello Mike", outputs[0])
    assertEquals("Hello Dave", outputs[1])
    assertEquals("Hello Katniss", outputs[2])
    inputs.removeAt(0)
    val change = change
    assertTrue(change.wasRemoved())
    assertEquals(2, outputs.size)
    assertEquals(1, change.removedSize)
    assertEquals("Hello Mike", change.removed[0])
    assertEquals("Hello Dave", outputs[0])
    inputs.removeAt(1)
    assertEquals(1, outputs.size)
    assertEquals("Hello Dave", outputs[0])
  }

  @Test
  @Throws(Exception::class)
  fun replace() {
    inputs.add("Mike")
    inputs.add("Dave")
    change
    change
    inputs[0] = "Bob"
    assertEquals("Hello Bob", outputs[0])
    val change = change
    assertTrue(change.wasReplaced())
    assertEquals("Hello Mike", change.removed[0])
    assertEquals("Hello Bob", change.addedSubList[0])
  } // Could also test permutation here if I could figure out how to actually apply one!
}
