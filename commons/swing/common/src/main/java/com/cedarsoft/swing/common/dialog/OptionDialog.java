package com.cedarsoft.swing.common.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.swing.common.Borders;
import com.cedarsoft.swing.common.Messages;
import com.cedarsoft.swing.common.SwingHelper;
import com.cedarsoft.swing.common.UiScaler;
import com.cedarsoft.swing.common.components.CButton;
import com.cedarsoft.unit.other.px;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.swing.StyledLabel;

import net.miginfocom.swing.MigLayout;

/**
 * Replaces JOptionPane
 */
public class OptionDialog extends StandardDialog {
  @px
  private static final int MIN_WIDTH = UiScaler.scale(300);
  @px
  private static final int MIN_HEIGHT = UiScaler.scale(160);

  @Nonnull
  private final Object message;
  @Nonnull
  private final String title;
  @Nullable
  private final ImageIcon icon;
  @Nonnull
  private final ButtonFactory buttonFactory;

  private OptionDialog(@Nullable JFrame parent, @Nonnull Object message, @Nonnull String title, @Nonnull ButtonFactory buttonFactory, @Nullable ImageIcon icon) {
    super(parent);

    this.buttonFactory = buttonFactory;
    this.message = message;
    this.title = title;
    this.icon = icon;

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    setDefaultCancelAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setDialogResult(ResultType.RESULT_CLOSED.getResult());
        setVisible(false);
        dispose();
      }
    });

    setTitle(title);
  }

  @Nonnull
  @UiThread
  public static ResultType showConfirmDialog(@Nullable Component parentComponent, @Nonnull Object message) {
    return showConfirmDialog(parentComponent, message, UIManager.getString("OptionPane.titleText"));
  }

  @Nonnull
  @UiThread
  public static ResultType showConfirmDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title) {
    return showConfirmDialog(parentComponent, message, title, OptionType.OK_CANCEL_OPTION);
  }

  @Nonnull
  @UiThread
  public static ResultType showConfirmDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory) {
    return showConfirmDialog(parentComponent, message, title, buttonFactory, MessageType.QUESTION_MESSAGE);
  }

  @Nonnull
  @UiThread
  public static ResultType showConfirmDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory, @Nonnull MessageType messageType) {
    return showConfirmDialog(parentComponent, message, title, buttonFactory, messageType, getIcon(messageType));
  }

  @Nonnull
  @UiThread
  public static ResultType showConfirmDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory, @Nonnull MessageType messageType, @Nullable ImageIcon icon) {
    return showOptionDialog(parentComponent, message, title, buttonFactory, messageType, icon);
  }

  @Nonnull
  @UiThread
  public static ResultType showMessageDialog(@Nullable Component parentComponent, @Nonnull Object message) {
    return showMessageDialog(parentComponent, message, UIManager.getString("OptionPane.messageDialogTitle"));
  }

  @Nonnull
  @UiThread
  public static ResultType showMessageDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title) {
    return showMessageDialog(parentComponent, message, title, OptionType.OK_OPTION);
  }

  @Nonnull
  @UiThread
  public static ResultType showMessageDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory) {
    return showMessageDialog(parentComponent, message, title, buttonFactory, MessageType.INFORMATION_MESSAGE);
  }

  @Nonnull
  @UiThread
  public static ResultType showMessageDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull MessageType messageType) {
    return showMessageDialog(parentComponent, message, title, OptionType.OK_OPTION, messageType);
  }

  @Nonnull
  @UiThread
  public static ResultType showMessageDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory, @Nonnull MessageType messageType) {
    return showMessageDialog(parentComponent, message, title, buttonFactory, messageType, getIcon(messageType));
  }

  @Nonnull
  @UiThread
  public static ResultType showMessageDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory, @Nonnull MessageType messageType, @Nullable ImageIcon icon) {
    return showOptionDialog(parentComponent, message, title, buttonFactory, messageType, icon);
  }

  @Nullable
  public static Integer showRadioDialog(@Nullable Component parentComponent, @Nonnull String message, @Nullable String title, @Nonnull MessageType messageType, int preselectedIndex, @Nonnull String... options) {
    JPanel radioPanel = new JPanel(new MigLayout("insets 0, wrap 1"));
    radioPanel.add(new JLabel(message), "gapbottom unrelated");

    ButtonGroup buttonGroup = new ButtonGroup();
    for (int i = 0; i < options.length; i++) {
      String option = options[i];
      JRadioButton radioButton = new JRadioButton(option);
      radioButton.setActionCommand(String.valueOf(i));

      if (i == preselectedIndex) {
        radioButton.setSelected(true);
      }

      radioPanel.add(radioButton);
      buttonGroup.add(radioButton);
    }

    ResultType resultType = showMessageDialog(parentComponent, radioPanel, title, OptionType.OK_CANCEL_OPTION, messageType);

    if (resultType != ResultType.RESULT_OK) {
      return null;
    }

    @Nullable ButtonModel selection = buttonGroup.getSelection();
    if (selection == null) {
      return null;
    }

    return Integer.parseInt(selection.getActionCommand());
  }

  @Nullable
  public static <T> T showComboBoxDialog(@Nullable Component parentComponent, @Nonnull String message, @Nullable String title, @Nonnull MessageType messageType, @Nonnull ComboBoxModel<T> model) {
    return showComboBoxDialog(parentComponent, message, title, messageType, model, null, null);
  }

  @Nullable
  public static <T> T showComboBoxDialog(@Nullable Component parentComponent, @Nonnull String message, @Nullable String title, @Nonnull MessageType messageType, @Nonnull ComboBoxModel<T> model, @Nullable Integer preselectedIndex, @Nullable ListCellRenderer<? super T> renderer) {
    JComboBox<T> comboBox = new JComboBox<>(model);
    if (preselectedIndex != null) {
      comboBox.setSelectedIndex(preselectedIndex);
    }
    if (renderer != null) {
      comboBox.setRenderer(renderer);
    }

    return showComboBoxDialog(parentComponent, message, title, messageType, comboBox);
  }

  @Nullable
  public static <T> T showComboBoxDialog(@Nullable Component parentComponent, @Nonnull String message, @Nullable String title, @Nonnull MessageType messageType, @Nonnull JComboBox<T> comboBox) {
    JPanel panel = new JPanel(new MigLayout("insets 0, fillx"));
    panel.add(new JLabel(message), "gapbottom unrelated");

    panel.add(comboBox);

    ResultType resultType = showMessageDialog(parentComponent, panel, title, OptionType.OK_CANCEL_OPTION, messageType);

    if (resultType != ResultType.RESULT_OK) {
      return null;
    }

    return (T) comboBox.getSelectedItem();
  }

  @Nonnull
  @UiThread
  private static ResultType showOptionDialog(@Nullable Component parentComponent, @Nonnull Object message, @Nullable String title, @Nonnull ButtonFactory buttonFactory, @Nonnull MessageType messageType, @Nullable ImageIcon icon) {
    JFrame parent = null;
    if (parentComponent != null) {
      Window windowAncestor = SwingUtilities.getWindowAncestor(parentComponent);
      if (windowAncestor instanceof JFrame) {
        parent = (JFrame) windowAncestor;
      }
    }
    if (parent == null) {
      parent = SwingHelper.getFrameSafe();
    }

    OptionDialog dialog = new OptionDialog(parent, message, title == null ? "" : title, buttonFactory, icon);
    dialog.setModal(true);
    dialog.setResizable(false);
    dialog.setVisible(true);
    return ResultType.forResult(dialog.getDialogResult());
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      pack();
      setLocationRelativeTo(getParent());
    }
    super.setVisible(visible);
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension preferredSize = super.getPreferredSize();

    //ensure a minimum size
    preferredSize.width = Math.max(preferredSize.width, MIN_WIDTH);
    preferredSize.height = Math.max(preferredSize.height, MIN_HEIGHT);

    return preferredSize;
  }

  @Override
  public JComponent createBannerPanel() {
    BannerPanel bannerPanel;
    if (icon == null) {
      bannerPanel = new BannerPanel(title);
    }
    else {
      bannerPanel = new BannerPanel(title, "", icon);
    }

    setTitle(title);
    return bannerPanel;
  }

  @Override
  public JComponent createContentPanel() {
    JPanel panel = new JPanel(new MigLayout("insets 0, fillx", "", ""));
    if (message instanceof Component) {
      panel.add((Component) message);
    }
    else {
      StyledLabel styledLabel = new StyledLabel(message.toString());
      styledLabel.setLineWrap(true);
      panel.add(styledLabel);
    }
    panel.setBorder(Borders.DIALOG_CONTENT_BORDER);
    return panel;
  }

  @Override
  public ButtonPanel createButtonPanel() {
    ButtonPanel buttonPanel = new ButtonPanel();

    buttonFactory.createButtons(this, buttonPanel);

    buttonPanel.setBorder(Borders.DIALOG_BUTTON_PANEL_BORDER);
    return buttonPanel;
  }

  @Nonnull
  public JButton createButton(@Nonnull ResultType forResult) {
    String actionName = getActionName(forResult);

    Action action = new AbstractAction(actionName) {
      @Override
      public void actionPerformed(ActionEvent e) {
        setDialogResult(forResult.getResult());
        setVisible(false);
        dispose();
      }
    };
    return new CButton(action);
  }

  @Nonnull
  private static String getActionName(@Nonnull ResultType resultType) {
    switch (resultType) {
      case RESULT_YES:
        return Messages.get("yes");
      case RESULT_NO:
        return Messages.get("no");
      case RESULT_CANCEL:
        return Messages.get("cancel");
      case RESULT_OK:
        return Messages.get("ok");
      case RESULT_CLOSED:
        //only for ESC or X to close dialog
        throw new IllegalArgumentException("Close button not supported");
      default:
    }

    throw new IllegalArgumentException("invalid result: " + resultType);
  }

  @Nonnull
  private static ImageIcon getIcon(@Nonnull MessageType messageType) {
    switch (messageType) {
      case INFORMATION_MESSAGE:
        return AbstractDialog.Icons.INFORMATION;
      case WARNING_MESSAGE:
        return AbstractDialog.Icons.WARNING;
      case ERROR_MESSAGE:
        return AbstractDialog.Icons.ERROR;
      case QUESTION_MESSAGE:
        return AbstractDialog.Icons.QUESTION;
      case PLAIN_MESSAGE:
        return AbstractDialog.Icons.INFORMATION;
      default:
        throw new IllegalArgumentException("invalid message type: " + messageType);
    }
  }

  public enum MessageType {
    INFORMATION_MESSAGE,
    WARNING_MESSAGE,
    ERROR_MESSAGE,
    QUESTION_MESSAGE,
    PLAIN_MESSAGE,
  }

  public enum OptionType implements ButtonFactory {
    YES_NO_OPTION,
    YES_NO_CANCEL_OPTION,
    OK_CANCEL_OPTION,
    OK_OPTION;

    @Override
    public void createButtons(@Nonnull OptionDialog optionDialog, @Nonnull ButtonPanel buttonPanel) {
      switch (this) {
        case YES_NO_OPTION:
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_YES), ButtonPanel.AFFIRMATIVE_BUTTON);
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_NO), ButtonPanel.CANCEL_BUTTON);
          return;
        case YES_NO_CANCEL_OPTION:
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_YES), ButtonPanel.AFFIRMATIVE_BUTTON);
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_NO), ButtonPanel.AFFIRMATIVE_BUTTON);
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_CANCEL), ButtonPanel.CANCEL_BUTTON);
          return;
        case OK_CANCEL_OPTION:
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_OK), ButtonPanel.AFFIRMATIVE_BUTTON);
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_CANCEL), ButtonPanel.CANCEL_BUTTON);
          return;
        case OK_OPTION:
          buttonPanel.addButton(optionDialog.createButton(ResultType.RESULT_OK), ButtonPanel.AFFIRMATIVE_BUTTON);
          return;
      }
      throw new UnsupportedOperationException("invalid option type: " + this);
    }
  }

  public enum ResultType {
    RESULT_CLOSED(-1),
    RESULT_YES(1),
    RESULT_NO(2),
    RESULT_CANCEL(3),
    RESULT_OK(4);

    private final int result;

    ResultType(int result) {
      this.result = result;
    }

    private int getResult() {
      return result;
    }

    @Nonnull
    private static ResultType forResult(int result) {
      for (ResultType resultType : ResultType.values()) {
        if (resultType.getResult() == result) {
          return resultType;
        }
      }
      throw new IllegalArgumentException("the given result <" + result + "> is not a valid result");
    }
  }

  /**
   * Fills the button panel with buttons
   */
  public static interface ButtonFactory {
    /**
     * Creates the buttons
     */
    void createButtons(@Nonnull OptionDialog optionDialog, @Nonnull ButtonPanel buttonPanel);
  }
}
