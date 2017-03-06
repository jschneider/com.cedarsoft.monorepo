package com.cedarsoft.exceptions.handling.notification;

import org.junit.*;

import javax.annotation.Nonnull;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NotificationDemo {
  @Test
  public void asdf() throws Exception {
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });

    Thread.sleep(1000);


    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        Notification notification = new Notification("daTitle", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.", new NotificationService.DetailsCallback() {
          @Override
          public void detailsClicked(@Nonnull Notification notification) {
            System.out.println("--> Details clicked <" + notification + ">");
          }
        });
        NotificationService notificationService = new NotificationService();
        notificationService.showNotification(notification);
      }
    });

    Thread.sleep(10000);
  }
}
