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

import com.cedarsoft.annotations.UiThread;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Abstract base class for JGoodies binding and validation related components
 *
 */
public abstract class ValidatingComponent<T> {
  @Nonnull
  protected final JPanel contentPane = new JPanel();

  @Nonnull
  protected final ValidatingPresentationModel<T> presentationModel;

  protected ValidatingComponent(@Nonnull ValidatingPresentationModel<T> presentationModel) {
    this.presentationModel = presentationModel;

    //Validation
    presentationModel.getValidationResultModel().addPropertyChangeListener(evt -> updateVisualization());
  }

  @UiThread
  protected void updateVisualization() {
    ValidationResultModel validationResultModel = presentationModel.getValidationResultModel();
    ValidationResult validationResult = validationResultModel.getResult();
    ValidationComponentUtils.updateComponentTreeSeverity(contentPane, validationResult);
    ValidationComponentUtils.updateComponentTreeSeverityBackground(contentPane, validationResult);
  }

  @Nonnull
  public JComponent getComponent() {
    return CIconFeedbackPanel.getWrappedComponentTree(presentationModel.getValidationResultModel(), contentPane);
  }

  @Nonnull
  public ValidationResultModel getValidationResultModel() {
    return presentationModel.getValidationResultModel();
  }

  @Nonnull
  public JPanel getContentPane() {
    return contentPane;
  }

  @Nonnull
  public ValidatingPresentationModel<T> getPresentationModel() {
    return presentationModel;
  }
}
