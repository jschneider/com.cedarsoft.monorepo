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
package com.cedarsoft.photos.tools.exif;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.*;

/**
 *
 */
public class ExifInfo3Test {
  private ExifInfo exifInfo;

  @BeforeEach
  public void setUp() throws Exception {
    InputStream in = getClass().getResourceAsStream("/exif-detailed3.txt");
    assertNotNull(in);
    exifInfo = new ExifInfo(in);
  }

  @Test
  public void testAperture() {
    assertEquals(3.3, exifInfo.getAperture(), 0);
  }

  @Test
  public void testDate() throws IOException {
    ZonedDateTime captureTime = exifInfo.getCaptureTime(ZoneId.of("Europe/Berlin"));
    assertThat(captureTime).isEqualTo(ZonedDateTime.of(2011, 12, 12, 17, 14, 16, 0, ZoneId.of("Europe/Berlin")));
  }

  @Test
  public void testExposure() throws ParseException {
    assertEquals(0.016666666666666666, exifInfo.getExposureTime(), 0);
    assertEquals("1/60", exifInfo.getExposureTimeFraction());
  }

  @Test
  public void testCrop() {
    assertEquals(4.3, exifInfo.getCropFactor(), 0);
  }

  @Test
  public void testIso() {
    assertEquals(800, exifInfo.getIso());
  }

  @Test
  public void testLens() {
    assertEquals(6, exifInfo.getFocalLength());
  }

  @Test
  public void testCamera() {
    assertEquals("FC  A4768048     592D31373032 2008:11:28 B4833022D6DC", exifInfo.getInternalSerial());
    assertEquals("FinePix F100fd", exifInfo.getModel());
    assertEquals("FUJIFILM", exifInfo.getMake());
  }
}
