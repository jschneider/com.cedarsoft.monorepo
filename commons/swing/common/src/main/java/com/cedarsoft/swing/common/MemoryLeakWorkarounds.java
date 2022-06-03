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
