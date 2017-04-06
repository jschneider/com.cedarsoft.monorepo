package com.cedarsoft.commons.swing.jgoodies;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

import javax.annotation.Nonnull;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.lang.reflect.Field;
import java.text.Format;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ComponentFactory {
  private ComponentFactory() {
  }

  @Nonnull
  public static <T> JComboBox<T> createComboBox(@Nonnull SelectionInList<? extends T> selectionInList) {
    JComboBox<T> comboBox = new JComboBox<>();
    Bindings.bind(comboBox, selectionInList);
    return comboBox;
  }

  @Nonnull
  public static <T> JComboBox<T> createComboBoxWithAutoCompletion(@Nonnull SelectionInList<? extends T> selectionInList, @Nonnull TextFilterator<? super T> filterator, @Nonnull Format format) {
    JComboBox<T> comboBox = createComboBox(selectionInList);
    installAutoCompletionSupport(comboBox, GlazedLists.eventList(selectionInList.getList()), filterator, format);
    return comboBox;
  }

  @Nonnull
  public static <E> AutoCompleteSupport<E> installAutoCompletionSupport(@Nonnull JComboBox<E> comboBox, @Nonnull EventList<E> items, @Nonnull TextFilterator<? super E> filterator, @Nonnull Format format) {
    Object initiallySelectedItem = comboBox.getSelectedItem();
    ComboBoxModel<E> originalModel = comboBox.getModel();

    AutoCompleteSupport<E> autoCompleteSupport = AutoCompleteSupport.install(comboBox, items, filterator, format);
    autoCompleteSupport.setFilterMode(TextMatcherEditor.CONTAINS);

    //Workaround: Set the initially selected item after installing the AutoCompleteSupport
    comboBox.setSelectedItem(initiallySelectedItem);

    //Workaround: Publish the events to the original model
    if (originalModel instanceof ComboBoxAdapter<?>) {
      AtomicBoolean isUpdating = new AtomicBoolean();

      comboBox.addItemListener(e -> {
        isUpdating.set(true);

        try {
          if (e.getStateChange() == ItemEvent.SELECTED) {
            originalModel.setSelectedItem(e.getItem());
          }
          if (e.getStateChange() == ItemEvent.DESELECTED) {
            originalModel.setSelectedItem(null);
          }
        } finally {
          isUpdating.set(false);
        }
      });


      try {
        Field selectionHolderField = ComboBoxAdapter.class.getDeclaredField("selectionHolder");
        selectionHolderField.setAccessible(true);
        ValueModel selectionHolderValue = (ValueModel) selectionHolderField.get(originalModel);
        selectionHolderValue.addValueChangeListener(evt -> {
          //Avoid endless events
          if (isUpdating.get()) {
            return;
          }
          comboBox.setSelectedItem(evt.getNewValue());
        });
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
    }
    return autoCompleteSupport;
  }
}