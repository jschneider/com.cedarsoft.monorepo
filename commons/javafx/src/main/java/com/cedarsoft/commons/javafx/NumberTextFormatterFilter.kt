package com.cedarsoft.commons.javafx

import javafx.scene.control.TextFormatter
import javafx.util.converter.NumberStringConverter
import java.util.function.Predicate
import java.util.function.UnaryOperator

/**
 * Processes text formatter changes. Ensures only valid numbers are added
 */
open class NumberTextFormatterFilter(
  private val converter: NumberStringConverter,
  private val filter: Predicate<Number>,
  private val emptyValue: Number,
) : UnaryOperator<TextFormatter.Change?> {

  override fun apply(change: TextFormatter.Change?): TextFormatter.Change? {
    if (change == null) {
      return null
    }

    // taken and adopted from https://stackoverflow.com/questions/40472668/numeric-textfield-for-integers-in-javafx-8-with-textformatter-and-or-unaryoperat#40472822

    // special case: empty text
    if (change.controlNewText.trim { it <= ' ' }.isEmpty()) {
      return if (!filter.test(emptyValue)) {
        null
      } else change
    }
    if ("-" == change.text) {
      if (isAllTextSelected(change)) {
        return change
      }

      // if user types or pastes a "-" in middle of current text, toggle sign of value:
      if (change.controlText.startsWith("-")) {
        // if we currently start with a "-", remove first character:
        change.text = ""
        change.setRange(0, 1)
        // since we're deleting a character instead of adding one,
        // the caret position needs to move back one, instead of
        // moving forward one, so we modify the proposed change to
        // move the caret two places earlier than the proposed change:
        change.caretPosition = change.caretPosition - 2
        change.anchor = change.anchor - 2
      } else {
        // otherwise just insert at the beginning of the text:
        change.setRange(0, 0)
        return change
      }
    }
    return try {
      val controlNewValue = converter.fromString(change.controlNewText)
      if (!filter.test(controlNewValue)) {
        null
      } else change
    } catch (ignored: Exception) {
      null
    }
  }

  companion object {
    private fun isAllTextSelected(change: TextFormatter.Change): Boolean {
      return change.rangeStart == 0 && change.rangeEnd == change.controlText.length
    }
  }
}
