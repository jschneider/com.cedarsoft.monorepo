package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * List cell that renders an enum
 */
public class EnumListCell<T extends Enum<T>> extends ListCell<T> {
  @Override
  protected void updateItem(T item, boolean empty) {
    super.updateItem(item, empty);
    if (item != null) {
      setText(EnumTranslatorUtil.getEnumTranslator().translate(item));
    }
  }

  public void apply(@Nonnull ComboBox<T> comboBox) {
    comboBox.setButtonCell(this);
    comboBox.setCellFactory(param -> new EnumListCell<>());
  }

  public static <T extends Enum<T>> void createFor(@Nonnull ComboBox<T> comboBox) {
    new EnumListCell<T>().apply(comboBox);
  }


}
