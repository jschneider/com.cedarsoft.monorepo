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
package com.cedarsoft.photos.tools.imagemagick;

import com.cedarsoft.image.Resolution;

import javax.annotation.Nonnull;

/**
 * Contains the image information returned from imagemagick "identify" command
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImageInformation {
  @Nonnull
  private final String type;
  @Nonnull
  private final Resolution resolution;

  public ImageInformation(@Nonnull String type, int width, int height) {
    this(type, new Resolution(width, height));
  }

  public ImageInformation(@Nonnull String type, @Nonnull Resolution resolution) {
    this.type = type;
    this.resolution = resolution;
  }

  @Nonnull
  public String getType() {
    return type;
  }

  @Nonnull
  public Resolution getResolution() {
    return resolution;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    ImageInformation that = (ImageInformation) obj;

    if (!type.equals(that.type)) {
      return false;
    }
    return resolution.equals(that.resolution);

  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + resolution.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "ImageInformation{" +
      "type='" + type + '\'' +
      ", resolution=" + resolution +
      '}';
  }
}
