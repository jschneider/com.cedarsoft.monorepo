package com.cedarsoft.swing.common;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import net.miginfocom.layout.LinkHandler;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * Contains helper methods to avoid memory leaks
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MemoryLeakWorkarounds {
  @NonUiThread
  public void activate() {
    workAroundJTreeRendererLeak();
    startMigLayoutCleanerTimer();
  }

  /**
   * Starts a time that works around a mig layout bug
   * Cleans up:
   * <ul>
   * <li>net.miginfocom.layout.LinkHandler.VALUES</li>
   * <li>net.miginfocom.layout.LinkHandler.VALUES_TEMP</li>
   * </ul>
   */
  @NonUiThread
  public void startMigLayoutCleanerTimer() {
    new Timer(1000 * 30, new ActionListener() {
      @Override
      @UiThread
      public void actionPerformed(ActionEvent e) {
        LinkHandler.getValue("", "", 1); //simulated read to enforce cleanup
      }
    }).start();
  }

  /**
   * Default renderers in JTree might hold some references
   */
  @NonUiThread
  public void workAroundJTreeRendererLeak() {
    try {
      SwingUtilities.invokeAndWait(() -> {
        JTree tree = new JTree();
        /*
         * Enforce creation of the default cell renderer (javax.swing.plaf.basic.BasicTreeUI.getBaseline)
         * that holds a reference to the last tree. (see #getTreeCellRendererComponent)
         */
        tree.getBaseline(0, 0);
      });
    } catch (InterruptedException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
