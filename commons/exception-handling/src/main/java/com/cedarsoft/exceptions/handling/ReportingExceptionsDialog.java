package com.cedarsoft.exceptions.handling;

import com.cedarsoft.swing.common.dialog.AbstractDialog;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonPanel;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Dialog for exceptions reporting progress
 */
public class ReportingExceptionsDialog extends AbstractDialog {
  public ReportingExceptionsDialog(@Nullable JFrame parent) {
    super(parent);
  }

  @Override
  public JComponent createBannerPanel() {
    BannerPanel bannerPanel = new BannerPanel(Messages.get("reporting.exception.title"), "");

    setTitle(Messages.get("reporting.exception.text"));

    return bannerPanel;
  }

  @Override
  public JComponent createContentPanel() {
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 1", "grow, fill"));
    panel.setOpaque(false);
    JProgressBar progressBar = new JProgressBar();
    progressBar.setIndeterminate(true);
    progressBar.setString(Messages.get("reporting.exception.title"));
    panel.add(progressBar, "w 400");

    panel.add(new JLabel(Messages.get("reporting.exception.text")));

    return panel;
  }

  @Nullable
  @Override
  public ButtonPanel createButtonPanel() {
    return null;
  }
}
