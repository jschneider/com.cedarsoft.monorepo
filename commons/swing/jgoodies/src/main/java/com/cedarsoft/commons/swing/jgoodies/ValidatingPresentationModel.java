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
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.DefaultValidationResultModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Presentation model that supports validation
 *
 */
public class ValidatingPresentationModel<B> extends PresentationModel<B> {
  @Nullable
  private final Validator<B> validator;
  @Nonnull
  private final ValidationResultModel validationResultModel = new DefaultValidationResultModel();

  public ValidatingPresentationModel(@Nonnull B bean, @Nullable Validator<B> validator) {
    super(bean);
    this.validator = validator;

    //every time the bean is updated the validation has to be recalculated
    addBeanPropertyChangeListener(evt -> updateValidation());
    getBeanChannel().addValueChangeListener(evt -> updateValidation());

    updateValidation();
  }

  /**
   * Refresh the validation result
   */
  @UiThread
  public final void updateValidation() {
    if (validator == null) {
      return;
    }

    ValidationResult validationResult = validator.validate(getBean());
    validationResultModel.setResult(validationResult);
  }

  @Nullable
  public Validator<B> getValidator() {
    return validator;
  }

  @UiThread
  @Nonnull
  public ValidationResultModel getValidationResultModel() {
    return validationResultModel;
  }

  @UiThread
  public void clearValidationResult() {
    getValidationResultModel().setResult(ValidationResult.EMPTY);
  }
}
