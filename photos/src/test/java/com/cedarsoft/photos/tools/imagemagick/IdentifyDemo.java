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

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.annotation.Nonnull;

import org.junit.*;
import org.junit.jupiter.api.Test;

import com.cedarsoft.image.Resolution;
import com.cedarsoft.photos.tools.CmdLineToolNotAvailableException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class IdentifyDemo {
  @Test
  public void it() throws Exception {
    Identify identify = createIdentify();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    identify.run(null, out, "/media/mule/data/media/photos/backend/5e/f17e82c55be6077735e6c968cfdc107967b8c6ec9a3b512c8af36430728afe/data");
    assertThat(out.toString()).isEqualTo("/media/mule/data/media/photos/backend/5e/f17e82c55be6077735e6c968cfdc107967b8c6ec9a3b512c8af36430728afe/data JPEG 2048x1536 2048x1536+0+0 8-bit sRGB 1.595MB 0.000u 0:00.000\n");

    ImageInformation information = identify.getImageInformation(new File("/media/mule/data/media/photos/backend/5e/f17e82c55be6077735e6c968cfdc107967b8c6ec9a3b512c8af36430728afe/data"));
    assertThat(information.getType()).isEqualTo("JPEG");
    assertThat(information.getResolution()).isEqualTo(new Resolution(2048, 1536));
  }

  @Nonnull
  public static Identify createIdentify() throws CmdLineToolNotAvailableException {
    File bin = new File("/usr/bin/identify");
    if (!bin.exists()) {
      throw new AssumptionViolatedException("Imagemagick not installed. Could not find identify");
    }
    return new Identify(bin);
  }
}
