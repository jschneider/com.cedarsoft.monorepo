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

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Contains an image and a volatile image that is recreated if necessary
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NotSoVolatileImage {
  private static final Logger LOG = Logger.getLogger(NotSoVolatileImage.class.getName());

  @Nonnull
  private final Lock lock = new ReentrantLock();

  @GuardedBy("lock")
  @Nonnull
  private VolatileImage volatileImage;
  @Nonnull
  private final Image image;

  /**
   * Creates a not so volatile image using the given image as source
   */
  @NonUiThread
  public NotSoVolatileImage(@Nonnull Image image) {
    this.image = image;
    volatileImage = SwingHelper.createVolatileImage(image.getWidth(null), image.getHeight(null));

    createVolatileImage(SwingHelper.getCurrentGraphicsConfiguration());
  }

  @AnyThread
  private void createVolatileImage(@Nonnull GraphicsConfiguration graphicsConfiguration) {
    lock.lock();
    try {
      //Check if the volatile image is valid
      int validationResult = volatileImage.validate(graphicsConfiguration);

      if (validationResult == VolatileImage.IMAGE_INCOMPATIBLE) {
        //The image is incompatible, therefore we have to create it later
        return;
      }

      if (validationResult != VolatileImage.IMAGE_RESTORED) {
        //The image could not be validated
        throw new IllegalStateException("validation failed with <" + validationResult + ">");
      }

      transferImageToVolatileImage();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Transfers the image to the volatile image
   */
  @AnyThread
  private void transferImageToVolatileImage() {
    lock.lock();
    try {
      //Clear the volatile image
      SwingHelper.clearVolatileImage(volatileImage);

      Graphics2D g2d = volatileImage.createGraphics();
      try {
        g2d.drawImage(image, 0, 0, null);
      } finally {
        g2d.dispose();
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Draws the volatile image to the given graphics context at the given location
   */
  @UiThread
  public void draw(@Nonnull Graphics2D g2d, int x, int y) {
    lock.lock();
    try {
      //repeat this until it has been done successfully
      do {
        GraphicsConfiguration deviceConfiguration = g2d.getDeviceConfiguration();

        int validationResult = volatileImage.validate(deviceConfiguration);
        if (validationResult == VolatileImage.IMAGE_INCOMPATIBLE) {
          //If the image is incompatible we have to recreate the volatile image
          LOG.warning("Volatile image is incompatible");

          //clean up first to avoid gc
          volatileImage.flush();
          //noinspection AssignmentToNull,ConstantConditions
          volatileImage = null;

          //recreate the volatile image
          volatileImage = deviceConfiguration.createCompatibleVolatileImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);

          //remember the best device configuration for later use
          SwingHelper.setLastUsedGraphicsConfiguration(deviceConfiguration);

          createVolatileImage(deviceConfiguration);
        }

        if (validationResult == VolatileImage.IMAGE_RESTORED) {
          LOG.warning("Volatile image has been restored");
          transferImageToVolatileImage();
        }

        //finally the volatile image
        g2d.drawImage(volatileImage, x, y, null);

      } while (volatileImage.contentsLost());
    } finally {
      lock.unlock();
    }
  }

  @AnyThread
  public void flush() {
    lock.lock();
    try {
      volatileImage.flush();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Returns the volatile image
   */
  @Nonnull
  @AnyThread
  public VolatileImage getVolatileImage() {
    lock.lock();
    try {
      return volatileImage;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Returns the original image
   */
  @Nonnull
  public Image getImage() {
    return image;
  }

  /**
   * Returns the width
   */
  @AnyThread
  public int getWidth() {
    int width = image.getWidth(null);
    if (width == -1) {
      throw new IllegalStateException("no width available");
    }
    return width;
  }

  /**
   * Returns the height
   */
  @AnyThread
  public int getHeight() {
    int height = image.getHeight(null);
    if (height == -1) {
      throw new IllegalStateException("no height available");
    }
    return height;
  }
}
