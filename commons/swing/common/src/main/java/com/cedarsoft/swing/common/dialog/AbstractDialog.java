package com.cedarsoft.swing.common.dialog;

import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.swing.common.Borders;
import com.cedarsoft.swing.common.IconUtils;
import com.cedarsoft.swing.common.Messages;
import com.cedarsoft.swing.common.UiScaler;
import com.cedarsoft.swing.common.components.CButton;
import com.cedarsoft.unit.other.px;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * Abstract base class for dialogs
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractDialog extends StandardDialog {
  @px
  public static final int MAX_WIDTH = UiScaler.scale(1280);
  @px
  public static final int MAX_HEIGHT = UiScaler.scale(1000);

  @Nullable
  private final ComponentSizeStorage componentSizeStorage;

  protected AbstractDialog(@Nullable JFrame parent) {
    this(parent, null);
  }

  protected AbstractDialog(@Nullable JFrame parent, @Nullable ComponentSizeStorage.Backend backend) {
    this(parent, backend, null);
  }

  protected AbstractDialog(@Nullable JFrame parent, @Nullable ComponentSizeStorage.Backend backend, @Nullable String additionalKey) {
    super(parent);

    if (backend == null) {
      componentSizeStorage = null;
    }
    else {
      componentSizeStorage = ComponentSizeStorage.connect(this, backend, this.getClass(), additionalKey);
    }

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    //Fallback if there is no
    setDefaultCancelAction(createSimpleCancelAction());
  }

  @Override
  protected void initComponents() {
    super.initComponents();
    applyBorders();
  }

  /**
   * Applies the default borders
   */
  protected void applyBorders() {
    getBannerPanel().setBorder(Borders.DEFAULT_5_PX);
    getContentPanel().setBorder(Borders.DIALOG_CONTENT_BORDER);

    @Nullable ButtonPanel buttonPanel = getButtonPanel();
    if (buttonPanel != null) {
      buttonPanel.setBorder(Borders.DIALOG_BUTTON_PANEL_BORDER);
    }
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      pack();

      //Workaround for dialogs. Overriding #getMinimuSize() is not enough
      setMinimumSize(getMinimumSize());

      //Restore the component size storage if there is one
      if (componentSizeStorage != null) {
        Dimension storedSize = componentSizeStorage.get();
        if (storedSize != null) {
          Dimension preferredSize = getPreferredSize();
          //ensure at least pref size
          storedSize.width = Math.max(storedSize.width, preferredSize.width);
          storedSize.height = Math.max(storedSize.height, preferredSize.height);
          setSize(storedSize);
        }
      }

      setLocationRelativeTo(getParent());
    }

    super.setVisible(b);
  }

  @Override
  public Dimension getMinimumSize() {
    if (_standardDialogPane != null) {
      return _standardDialogPane.getMinimumSize();
    }

    return super.getMinimumSize();
  }

  @Override
  public Dimension getMaximumSize() {
    Dimension maximumSize = super.getMaximumSize();
    maximumSize.width = Math.min(maximumSize.width, MAX_WIDTH);
    maximumSize.height = Math.min(maximumSize.height, MAX_HEIGHT);
    return maximumSize;
  }

  @Nonnull
  protected final Action createSimpleCancelAction() {
    return new AbstractAction(Messages.get("cancel")) {
      @Override
      public void actionPerformed(ActionEvent e) {
        cancel();
      }
    };
  }

  @Nonnull
  protected final Action createSimpleOkAction() {
    return new AbstractAction(Messages.get("ok")) {
      @Override
      public void actionPerformed(ActionEvent e) {
        ok();
      }
    };
  }

  @UiThread
  public void cancel() {
    setDialogResult(RESULT_CANCELLED);
    setVisible(false);
    dispose();
  }

  /**
   * @noinspection InstanceMethodNamingConvention
   */
  @UiThread
  public void ok() {
    setDialogResult(RESULT_AFFIRMED);
    setVisible(false);
    dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    @Nonnull Dimension preferredSize = super.getPreferredSize();
    preferredSize.width = Math.min(MAX_WIDTH, preferredSize.width);
    preferredSize.height = Math.min(MAX_HEIGHT, preferredSize.height);
    return preferredSize;
  }

  @Override
  @Nullable
  public ButtonPanel createButtonPanel() {
    return createOkButtonPanel();
  }

  @Nonnull
  protected ButtonPanel createOkButtonPanel() {
    return createButtonPanel(createSimpleOkAction(), null);
  }

  @Nonnull
  protected ButtonPanel createButtonPanel(@Nullable Action oktAction, @Nullable Action cancelAction, @Nonnull Action... otherActions) {
    ButtonPanel buttonPanel = new ButtonPanel();

    if (oktAction != null) {
      CButton affirmativeButton = new CButton(oktAction);
      buttonPanel.addButton(affirmativeButton, ButtonPanel.AFFIRMATIVE_BUTTON);
      getRootPane().setDefaultButton(affirmativeButton);
    }
    setDefaultAction(oktAction);

    for (Action otherAction : otherActions) {
      buttonPanel.addButton(new CButton(otherAction), ButtonPanel.OTHER_BUTTON);
    }

    if (cancelAction != null) {
      CButton cancelButton = new CButton(cancelAction);
      buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);
      setDefaultCancelAction(cancelAction);
    }

    return buttonPanel;
  }


  /**
   * Sets the dialog visible using SwingUtilities#invokeLater
   */
  @NonBlocking
  @NonUiThread
  @UiThread
  public void setVisibleNonBlocking() {
    SwingUtilities.invokeLater(() -> setVisible(true));
  }


  /**
   * Contains the icons for usage in dialogs
   */
  public interface Icons {
    @Nonnull
    ImageIcon INFORMATION = new ImageIcon(IconUtils.getResource("com/cedarsoft/swing/common/dialog/info.png"));
    @Nonnull
    ImageIcon WARNING = new ImageIcon(IconUtils.getResource("com/cedarsoft/swing/common/dialog/warning.png"));
    @Nonnull
    ImageIcon ERROR = new ImageIcon(IconUtils.getResource("com/cedarsoft/swing/common/dialog/error.png"));
    @Nonnull
    ImageIcon QUESTION = new ImageIcon(IconUtils.getResource("com/cedarsoft/swing/common/dialog/question.png"));

    /**
     * Busy icon that can be used to mark a button as busy
     */
    @Nonnull
    ImageIcon BUSY = new ImageIcon(IconUtils.getResource("com/cedarsoft/swing/common/dialog/busy.gif"));
  }
}
