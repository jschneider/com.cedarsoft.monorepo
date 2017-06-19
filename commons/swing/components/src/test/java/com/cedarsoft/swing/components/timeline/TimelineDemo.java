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
package com.cedarsoft.swing.components.timeline;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.google.common.collect.ImmutableList;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TimelineDemo {
  public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
    UIManager.setLookAndFeel(new NimbusLookAndFeel());

    ImmutableList.Builder<TimelineModel.BarValues> barValues = ImmutableList.builder();

    final LocalDate start = LocalDate.of(2017, 1, 1);
    LocalDate end = LocalDate.of(2017, 11, 12);

    final int days = (int) (ChronoUnit.DAYS.between(start, end) + 1);
    for (int i = 0; i < days; i++) {
      barValues.add(createBarValues());
    }

    final AbstractTimelineModel model = new AbstractTimelineModel() {
      @Override
      public boolean isValid() {
        return true;
      }
    };
    model.update(start, barValues.build());

    model.setVisibleArea(new TimelineModel.VisibleArea(0, 10));

    show(model, dayIndex -> {
      System.out.println("Navigating to " + start.plusDays(dayIndex) + " for index <" + dayIndex + ">");
      model.setVisibleArea(new TimelineModel.VisibleArea(dayIndex - 10, dayIndex));
    });
  }

  @Nonnull
  private static TimelineModel.BarValues createBarValues() {
    return new ConstantBarValues(Math.random() * 50, Math.random() * 10, Math.random() * 5);
  }

  private static void show(@Nonnull TimelineModel model, @Nonnull Timeline.NavigationHandler navigationHandler) {
    final JFrame frame = new JFrame();

    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(new JScrollPane(new Timeline(model, navigationHandler)), BorderLayout.NORTH);
    frame.getContentPane().add(new JLabel("Data here!"), BorderLayout.CENTER);

    frame.setSize(1024, 768);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

}
