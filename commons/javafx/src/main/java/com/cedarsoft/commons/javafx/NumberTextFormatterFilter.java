package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

/**
 * Processes text formatter changes. Ensures only valid numbers are added
 */
class NumberTextFormatterFilter implements UnaryOperator<TextFormatter.Change> {
  @Nonnull
  private final NumberStringConverter converter;
  @Nonnull
  private final Predicate<Number> filter;
  @Nonnull
  private final Number emptyValue;

  NumberTextFormatterFilter(@Nonnull NumberStringConverter converter, @Nonnull Predicate<Number> filter, @Nonnull Number emptyValue) {
    this.converter = converter;
    this.filter = filter;
    this.emptyValue = emptyValue;
  }

  @Nullable
  @Override
  public TextFormatter.Change apply(TextFormatter.Change change) {
    // taken and adopted from https://stackoverflow.com/questions/40472668/numeric-textfield-for-integers-in-javafx-8-with-textformatter-and-or-unaryoperat#40472822

    // special case: empty text
    if (change.getControlNewText().trim().isEmpty()) {
      if (!filter.test(emptyValue)) {
        return null;
      }
      return change;
    }

    if ("-".equals(change.getText())) {
      if (isAllTextSelected(change)) {
        return change;
      }

      // if user types or pastes a "-" in middle of current text, toggle sign of value:
      if (change.getControlText().startsWith("-")) {
        // if we currently start with a "-", remove first character:
        change.setText("");
        change.setRange(0, 1);
        // since we're deleting a character instead of adding one,
        // the caret position needs to move back one, instead of
        // moving forward one, so we modify the proposed change to
        // move the caret two places earlier than the proposed change:
        change.setCaretPosition(change.getCaretPosition() - 2);
        change.setAnchor(change.getAnchor() - 2);
      }
      else {
        // otherwise just insert at the beginning of the text:
        change.setRange(0, 0);
        return change;
      }
    }

    try {
      Number controlNewValue = converter.fromString(change.getControlNewText());
      if (!filter.test(controlNewValue)) {
        return null;
      }
      return change;
    }
    catch (Exception ignored) {
      return null;
    }
  }

  private static boolean isAllTextSelected(@Nonnull TextFormatter.Change change) {
    return change.getRangeStart() == 0 && change.getRangeEnd() == change.getControlText().length();
  }
}
