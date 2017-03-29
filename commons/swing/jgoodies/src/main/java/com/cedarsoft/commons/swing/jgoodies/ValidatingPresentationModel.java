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
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
