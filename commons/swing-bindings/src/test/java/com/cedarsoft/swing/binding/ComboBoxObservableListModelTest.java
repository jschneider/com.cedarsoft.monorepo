package com.cedarsoft.swing.binding;

import org.junit.jupiter.api.*;

import com.google.common.collect.ImmutableList;

/**
 */
public class ComboBoxObservableListModelTest {
  @Test
  void testInstantiationFromJava() throws Exception {
    ComboBoxObservableListModel<String> withVarArgs = new ComboBoxObservableListModel<>("a", "b", "C");
    ComboBoxObservableListModel<String> withCollection = new ComboBoxObservableListModel<>(ImmutableList.of("a", "b", "C"));
    ImmutableList<? extends String> args = ImmutableList.of("a", "b", "C");
    ComboBoxObservableListModel<String> withCollection2 = new ComboBoxObservableListModel<>(args);
  }
}
