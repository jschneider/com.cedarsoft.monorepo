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
package com.cedarsoft.photos;

import com.cedarsoft.image.Resolution;
import com.cedarsoft.photos.di.Modules;
import com.cedarsoft.photos.tools.exif.ExifHelper;
import com.cedarsoft.photos.tools.exif.ExifInfo;
import com.cedarsoft.photos.tools.imagemagick.Convert;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CreatePreviewsRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("Creating preview files");

    Injector injector = Guice.createInjector(Modules.getModules());
    Convert convert = injector.getInstance(Convert.class);
    ExifHelper exifHelper = injector.getInstance(ExifHelper.class);

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    imageFinder.find((storage, dataFile, hash) -> {
      try {
        ExifInfo exifInfo = exifHelper.getExifInfo(hash);
        String extension = exifInfo.getFileTypeExtension();

        File dir = dataFile.getParentFile();
        dir.setWritable(true);
        try {
          File thumbsDir = new File(dir, "thumbs");
          thumbsDir.mkdir();

          createThumb(convert, dataFile, thumbsDir, new Resolution(1920, 1080), extension);
          createThumb(convert, dataFile, thumbsDir, new Resolution(260, 260), extension);
        } finally {
          dir.setWritable(false);
        }
      } catch (ExifHelper.NoExifInfoFoundException ignore) {
        System.out.println("--> No exif available for <" + dataFile.getAbsolutePath() + ">");
      }
    });
  }

  private static void createThumb(@Nonnull Convert convert, @Nonnull File dataFile, @Nonnull File thumbsDir, @Nonnull Resolution maxResolution, @Nonnull String inTypeMagick) throws IOException {
    File thumbFile = new File(thumbsDir, maxResolution.getWidth() + "x" + maxResolution.getHeight());
    if (thumbFile.exists()) {
      return;
    }

    convert.createThumbnail(dataFile, thumbFile, maxResolution, inTypeMagick);
  }
}
