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

import com.cedarsoft.photos.di.Modules;
import com.cedarsoft.photos.tools.exif.ExifHelper;
import com.cedarsoft.photos.tools.exif.ExifInfo;
import com.cedarsoft.photos.tools.imagemagick.Identify;
import com.cedarsoft.photos.tools.imagemagick.ImageInformation;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

/**
 * Finds small images
 *
 */
public class DeleteSmallImagesRunner {
  public static void main(String[] args) throws IOException {
    Injector injector = Guice.createInjector(Modules.getModules());

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    Identify identify = injector.getInstance(Identify.class);
    ExifHelper exifHelper = injector.getInstance(ExifHelper.class);

    imageFinder.find((storage, dataFile, hash) -> {
      try {
        System.out.print(".");
        try {
          ExifInfo exifInfo = exifHelper.getExifInfo(hash);
          if (LinkByDateCreator.isRaw(exifInfo.getFileTypeExtension())) {
            return;
          }
        } catch (ExifHelper.NoExifInfoFoundException ignore) {
          System.out.println("No exif found for <" + hash + ">");
          return;
        }

        ImageInformation imageInformation = identify.getImageInformation(dataFile);
        if ((imageInformation.getResolution().getWidth() < 2000) && (imageInformation.getResolution().getHeight() < 2000)) {
          System.out.println("\n--> Small Image: " + imageInformation.getResolution() + " - " + dataFile.getAbsolutePath());

          storage.delete(hash);
        }
      } catch (Exception e) {
        System.err.println("Problem when checking " + dataFile.getAbsolutePath());
        e.printStackTrace();
        System.exit(1);
      }
    });
  }
}
