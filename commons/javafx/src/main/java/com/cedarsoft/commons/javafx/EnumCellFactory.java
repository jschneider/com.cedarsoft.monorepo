package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * A cell factory that renders enum values using an {@link EnumTranslator}.
 *
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class EnumCellFactory<S, T extends Enum<?>> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
  @Nonnull
  private final EnumTranslator enumTranslator;

  public EnumCellFactory(@Nonnull EnumTranslator enumTranslator) {
    this.enumTranslator = enumTranslator;
  }

  @Override
  public TableCell<S, T> call(TableColumn<S, T> param) {
    return new TableCell<S, T>() {
      @Override
      protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          this.setText(null);
          this.setGraphic(null);
        }
        else {
          this.setText(enumTranslator.translate(item));
        }
      }
    };
  }
}


