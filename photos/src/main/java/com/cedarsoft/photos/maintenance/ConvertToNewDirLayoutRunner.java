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
package com.cedarsoft.photos.maintenance;

import com.cedarsoft.photos.ImageStorage;
import com.cedarsoft.photos.di.Modules;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.io.IOException;

/**
 * Converts to new backend file format
 *
 */
public class ConvertToNewDirLayoutRunner {
  public static void main(String[] args) throws IOException, InterruptedException {
    if (true) {
      throw new UnsupportedOperationException("Are you sure you know what you are doing? I don't think so. This main will *destroy* data");
    }

    Injector injector = Guice.createInjector(Modules.getModules());

    ImageStorage imageStorage = injector.getInstance(ImageStorage.class);
    File baseDir = imageStorage.getDeletedBaseDir();

    for (File firstPartDir : baseDir.listFiles()) {
      String firstPart = firstPartDir.getName();

      for (File leftoverDir : firstPartDir.listFiles()) {
        String leftOver = leftoverDir.getName();

        if (leftOver.length() == 64) {
          //Already converted
          continue;
        }

        if (leftOver.length() != 62) {
          throw new IllegalStateException("Invalid dir <" + leftoverDir.getAbsolutePath() + ">");
        }

        String completeHashAsString = firstPart + leftOver;
        if (completeHashAsString.length() != 64) {
          throw new IllegalStateException("Invalid complete hash <" + completeHashAsString + "> for <" + leftoverDir.getAbsolutePath() + ">");
        }

        //Now rename
        File targetDir = new File(leftoverDir.getParent(), completeHashAsString);
        System.out.println("--> Renaming " + leftoverDir.getAbsolutePath() + " to " + targetDir.getAbsolutePath());
        if (!leftoverDir.renameTo(targetDir)) {
          throw new IOException("Could not rename <" + leftoverDir.getAbsolutePath() + "> to <" + targetDir.getAbsolutePath() + ">");
        }
      }
    }
  }
}
