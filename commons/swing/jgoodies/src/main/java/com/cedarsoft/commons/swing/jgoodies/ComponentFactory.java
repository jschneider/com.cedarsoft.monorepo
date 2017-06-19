/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
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
import javax.annotation.Nullable;
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
    return createComboBox(selectionInList, null);
  }

  @Nonnull
  public static <T> JComboBox<T> createComboBox(@Nonnull SelectionInList<? extends T> selectionInList, @Nullable String nullText) {
    JComboBox<T> comboBox = new JComboBox<>();
    if (nullText != null) {
      Bindings.bind(comboBox, selectionInList, nullText);
    }
    else {
      Bindings.bind(comboBox, selectionInList);
    }
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
