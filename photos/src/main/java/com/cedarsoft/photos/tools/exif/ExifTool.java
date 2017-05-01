/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.photos.tools.exif;

import com.cedarsoft.photos.tools.AbstractCommandLineTool;
import com.google.errorprone.annotations.Immutable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * Wrapper for exiftool
 */
//TODO add SequenceNumber
@Immutable
public class ExifTool extends AbstractCommandLineTool {

  public ExifTool(@Nonnull File bin) {
    super(bin);
  }

  public ExifTool(@Nonnull String bin) {
    this(new File(bin));
  }

  public void clearRotation(@Nonnull File target) throws IOException {
    run("-P", "-overwrite_original", "-Orientation=normal", target.getAbsolutePath());
  }
}
