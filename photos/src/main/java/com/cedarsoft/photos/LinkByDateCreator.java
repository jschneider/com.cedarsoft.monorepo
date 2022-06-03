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

import com.cedarsoft.crypt.Hash;
import com.cedarsoft.exceptions.NotFoundException;
import com.cedarsoft.io.LinkUtils;
import com.cedarsoft.photos.tools.exif.ExifExtractor;
import com.cedarsoft.photos.tools.exif.ExifInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Creates symlinks by date
 *
 */
public class LinkByDateCreator {
  @Nonnull
  private final File baseDir;
  @Nonnull
  private final ExifExtractor exifExtractor;

  public LinkByDateCreator(@Nonnull File baseDir, @Nonnull ExifExtractor exifExtractor) {
    this.baseDir = baseDir;
    this.exifExtractor = exifExtractor;
  }

  /**
   * Creates a link for the given source file
   */
  @Nonnull
  public File createLink(@Nonnull File sourceFile, @Nonnull Hash hash) throws IOException, NotFoundException {
    try (FileInputStream in = new FileInputStream(sourceFile)) {
      ExifInfo exifInfo = exifExtractor.extractInfo(in);

      String extension = exifInfo.getFileTypeExtension();

      String shortHashString = hash.getValueAsHex().substring(0, 8);

      @Nullable ZonedDateTime captureTime = exifInfo.getCaptureTimeNullable(ZoneOffset.UTC);
      String fileNameSuffix = exifInfo.getCameraId() + "_" + exifInfo.getFileNumberSafe() + "_#" + shortHashString + "." + extension;
      if (captureTime == null) {
        return createUnknownDateLink(sourceFile, fileNameSuffix, extension);
      }

      String fileName = captureTime.format(DateTimeFormatter.ISO_DATE_TIME) + "_" + fileNameSuffix;

      //Store in the month
      createLink(sourceFile, new File(createMonthDir(captureTime), "all"), fileName, extension);

      //Store in the day
      createLink(sourceFile, new File(createDayDir(captureTime), "all"), fileName, extension);

      //Store in the hour
      return createLink(sourceFile, createHourDir(captureTime), fileName, extension);
    }
  }

  @Nonnull
  private File createUnknownDateLink(@Nonnull File sourceFile, @Nonnull String targetFileName, @Nonnull String extension) throws IOException {
    File targetDir = createUnknownDateDir();
    return createLink(sourceFile, targetDir, targetFileName, extension);
  }

  /**
   * Creates a link
   */
  @Nonnull
  private File createLink(@Nonnull File sourceFile, @Nonnull File targetDir, @Nonnull String targetFileName, @Nonnull String extension) throws IOException {
    //The target directory (uses the extension as directory name)
    File targetDirWithExtension;
    if (isRaw(extension)) {
      targetDirWithExtension = new File(targetDir, extension);
    }
    else {
      targetDirWithExtension = targetDir;
    }
    ensureDirExists(targetDirWithExtension);

    int suffixCounter = 0;
    while (true) {
      try {
        File targetFile = new File(targetDirWithExtension, addSuffix(targetFileName, suffixCounter));
        LinkUtils.createSymbolicLink(sourceFile, targetFile);
        return targetFile;
      } catch (LinkUtils.AlreadyExistsWithOtherTargetException ignore) {
        //Something went wrong
        suffixCounter++;
      }
    }
  }

  @Nonnull
  static String addSuffix(@Nonnull String targetFileName, int suffixCounter) {
    if (suffixCounter == 0) {
      //Do not add a "0"
      return targetFileName;
    }

    int lastIndex = targetFileName.lastIndexOf('.');
    if (lastIndex < 0) {
      return targetFileName + "_" + suffixCounter;
    }

    String firstPart = targetFileName.substring(0, lastIndex);
    String lastPart = targetFileName.substring(lastIndex);

    return firstPart + "_" + suffixCounter + lastPart;
  }

  //TODO move
  public static boolean isRaw(@Nonnull String extension) {
    return extension.equalsIgnoreCase("cr2");
  }

  private void ensureDirExists(File hourDir) throws IOException {
    if (!hourDir.isDirectory()) {
      if (!hourDir.mkdirs()) {
        throw new IOException("Could not create <" + hourDir.getAbsolutePath() + ">");
      }
    }
  }

  @Nonnull
  private File createHourDir(@Nonnull ZonedDateTime time) {
    File dayDir = createDayDir(time);
    return new File(dayDir, formatTwoDigits(time.getHour()));
  }

  @Nonnull
  private File createDayDir(@Nonnull ZonedDateTime time) {
    File monthDir = createMonthDir(time);
    String dirName = formatTwoDigits(time.getDayOfMonth()) + "_" + time.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
    return new File(monthDir, dirName);
  }

  @Nonnull
  private File createMonthDir(@Nonnull ZonedDateTime time) {
    File yearDir = createYearDir(time);
    String dirName = formatTwoDigits(time.getMonth().getValue()) + "_" + time.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
    return new File(yearDir, dirName);
  }

  @Nonnull
  private File createYearDir(@Nonnull ZonedDateTime time) {
    return new File(baseDir, String.valueOf(time.getYear()));
  }

  @Nonnull
  private File createUnknownDateDir() {
    return new File(baseDir, "unknown-date");
  }

  @Nonnull
  private static String formatTwoDigits(int value) {
    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    numberFormat.setMinimumIntegerDigits(2);
    return numberFormat.format(value);
  }
}
