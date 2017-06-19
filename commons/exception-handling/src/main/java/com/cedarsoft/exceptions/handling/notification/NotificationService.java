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
package com.cedarsoft.exceptions.handling.notification;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.exceptions.handling.Messages;
import com.cedarsoft.swing.common.Fonts;
import com.cedarsoft.swing.common.SwingHelper;
import com.cedarsoft.swing.common.dialog.AbstractDialog;
import com.cedarsoft.unit.si.ms;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;
import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.styles.ToolTipBalloonStyle;
import net.java.balloontip.utils.TimingUtils;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Shows notifications in a popup
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@ThreadSafe
public class NotificationService {
  @ms
  public static final int VISIBILITY_TIME = 15000;
  @Nonnull
  private static final ToolTipBalloonStyle STYLE = new ToolTipBalloonStyle(new Color(255, 251, 192), Color.GRAY);

  @UiThread
  @Nonnull
  private final Queue<Notification> notificationQueue = new LinkedList<>();

  private boolean notificationVisible;

  @UiThread
  public void showNotification(@Nonnull Notification notification) {
    if (notificationVisible) {
      notificationQueue.add(notification);
      return;
    }

    @Nullable JFrame frame = SwingHelper.getFrameSafe();
    if (frame == null) {
      throw new IllegalArgumentException("No frame found");
    }
    JComponent mainFrameComponent = (JComponent) frame.getContentPane();

    BalloonTip balloonTip = new CustomBalloonTip(mainFrameComponent, new JPanel(), new Rectangle(10, 10), STYLE, new BottomRightPositioner(mainFrameComponent), BalloonTip.getDefaultCloseButton()) {
      @Override
      public void closeBalloon() {
        super.closeBalloon();
        notificationVisible = false;

        //Look if there is a notification available
        @Nullable Notification nextNotification = notificationQueue.poll();
        if (nextNotification == null) {
          return;
        }

        //Wait for 1200 ms until the notification is shown
        Timer timer = new Timer(1200, e -> showNotification(nextNotification));
        timer.setRepeats(false);
        timer.start();
      }
    };

    JPanel content = new JPanel(new MigLayout("fill, wrap 2", "[][grow,fill]", "[fill][fill,grow][fill]"));
    content.setOpaque(false);

    JLabel headlineLabel = new JLabel(notification.getTitle());
    headlineLabel.setFont(Fonts.TITLE);

    content.add(new JLabel(AbstractDialog.Icons.WARNING));
    content.add(headlineLabel, "wrap");
    content.add(new JLabel(notification.getMessage()), "span");

    @Nullable DetailsCallback detailsCallback = notification.getDetailsCallback();
    if (detailsCallback != null) {
      content.add(SwingHelper.createHyperLink(new AbstractAction(Messages.get("details")) {
        @Override
        public void actionPerformed(ActionEvent e) {
          balloonTip.closeBalloon();
          detailsCallback.detailsClicked(notification);
        }
      }), "alignx right, span");
    }

    balloonTip.setContents(content);


    notificationVisible = true;
    TimingUtils.showTimedBalloon(balloonTip, VISIBILITY_TIME);
  }

  /**
   * Positions the balloon at the bottom right
   */
  private static class BottomRightPositioner extends BalloonTipPositioner {
    @Nonnull
    private final JComponent mainFrameComponent;

    BottomRightPositioner(@Nonnull JComponent mainFrameComponent) {
      this.mainFrameComponent = mainFrameComponent;
    }

    @Override
    public Point getTipLocation() {
      throw new UnsupportedOperationException("not available");
    }

    @Override
    public void determineAndSetLocation(Rectangle attached) {
      Dimension preferredSize = balloonTip.getPreferredSize();

      double x = mainFrameComponent.getWidth() - preferredSize.getWidth() - 10;
      double y = mainFrameComponent.getHeight() - preferredSize.getHeight() - 15;

      balloonTip.setSize(preferredSize);
      balloonTip.setLocation((int) x, (int) y);
      balloonTip.revalidate();
    }

    @Override
    protected void onStyleChange() {
    }
  }

  @FunctionalInterface
  public interface DetailsCallback {
    @UiThread
    void detailsClicked(@Nonnull Notification notification);
  }
}
