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

    JComponent mainFrameComponent = (JComponent) SwingHelper.getFocusedFrame().getContentPane();

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
