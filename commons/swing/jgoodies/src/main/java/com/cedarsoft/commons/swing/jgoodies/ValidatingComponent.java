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
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
