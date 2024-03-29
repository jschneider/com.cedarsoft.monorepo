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
import com.cedarsoft.photos.tools.exif.ExifExtractor;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 */
public class ExtractExifsRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("Extracting exif files");

    Injector injector = Guice.createInjector(Modules.getModules());
    ExifExtractor exifExtractor = injector.getInstance(ExifExtractor.class);

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    imageFinder.find((storage, dataFile, hash) -> {
      File dir = dataFile.getParentFile();
      File exifFile = new File(dir, "exif");
      if (exifFile.exists()) {
        return;
      }

      File tmpOut = new File(dir, "exif.tmp");
      try {
        dir.setWritable(true);
        try (FileInputStream in = new FileInputStream(dataFile); FileOutputStream out = new FileOutputStream(tmpOut)) {
          exifExtractor.extractDetailed(in, out);
        }

        //Rename after success
        tmpOut.renameTo(exifFile);
      } catch (IOException ignore) {
        System.out.println("Failed: " + dataFile.getAbsolutePath());
      } finally {
        tmpOut.delete();
        dir.setWritable(false);
      }
    });
  }
}
