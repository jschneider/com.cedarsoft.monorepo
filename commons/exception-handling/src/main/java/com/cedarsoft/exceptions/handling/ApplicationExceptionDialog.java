package com.cedarsoft.exceptions.handling;

import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.swing.common.dialog.AbstractDialog;
import com.cedarsoft.swing.common.dialog.ComponentSizeStorage;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.swing.StyledLabel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Shows an application exception
 */
public class ApplicationExceptionDialog extends AbstractDialog {
  @Nonnull
  private final ApplicationException e;

  public ApplicationExceptionDialog(@Nullable JFrame parent, @Nonnull ApplicationException e, @Nullable ComponentSizeStorage.Backend preferences) {
    super(parent, preferences);
    this.e = e;
  }

  @Override
  public JComponent createBannerPanel() {
    BannerPanel bannerPanel = new BannerPanel(e.getErrorCode().toString(), e.getTitle());
    setTitle(e.getTitle() + " (" + e.getErrorCode() + ")");
    return bannerPanel;
  }

  @Override
  public JComponent createContentPanel() {
    JPanel panel = new JPanel(new BorderLayout()) {
      @Override
      public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = Math.max(preferredSize.width, 500);
        return preferredSize;
      }
    };

    StyledLabel styledLabel = new StyledLabel(e.getLocalizedMessage());
    panel.add(styledLabel);

    styledLabel.setVerticalAlignment(SwingConstants.TOP);
    styledLabel.setRows(5);
    styledLabel.setMaxRows(7);
    styledLabel.setLineWrap(true);

    return panel;
  }

}
