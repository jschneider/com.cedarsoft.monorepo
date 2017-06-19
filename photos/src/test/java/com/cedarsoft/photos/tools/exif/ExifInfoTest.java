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

import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 *
 */
public class ExifInfoTest {
  private ExifInfo exifInfo;

  @Before
  public void setUp() throws Exception {
    InputStream in = getClass().getResourceAsStream("/exif-detailed.txt");
    assertNotNull(in);
    exifInfo = new ExifInfo(in);
  }

  @Test
  public void testAperture() {
    assertEquals(2.8, exifInfo.getAperture(), 0);
  }

  @Test
  public void testDate() throws IOException {
    ZonedDateTime captureTime = exifInfo.getCaptureTime(ZoneId.of("Europe/Berlin"));
    assertThat(captureTime).isEqualTo(ZonedDateTime.of(2009, 10, 24, 14, 01, 46, 0, ZoneId.of("Europe/Berlin")));
  }

  @Test
  public void testExposure() throws ParseException {
    assertEquals(0.00125, exifInfo.getExposureTime(), 0);
    assertEquals("1/800", exifInfo.getExposureTimeFraction());
  }

  @Test
  public void testImNu() {
    assertEquals(2483, exifInfo.getFileNumber());
  }

  @Test
  public void testCrop() {
    assertEquals(1.6, exifInfo.getCropFactor(), 0);
  }

  @Test
  public void testIso() {
    assertEquals(125, exifInfo.getIso());
  }

  @Test
  public void testLens() {
    assertEquals("Canon EF 70-200mm f/2.8L IS", exifInfo.getLensType());
    assertEquals(70, exifInfo.getMinFocalLength());
    assertEquals(200, exifInfo.getMaxFocalLength());
    assertEquals(70, exifInfo.getFocalLength());
    assertEquals(32.0, exifInfo.getMinAperture(), 0);
    assertEquals(2.8, exifInfo.getMaxAperture(), 0);
  }

  @Test
  public void testCamera() {
    assertEquals("0230101649", exifInfo.getCameraSerial());
    assertEquals("S0027533", exifInfo.getInternalSerial());
    assertEquals("Canon EOS 7D", exifInfo.getModel());
    assertEquals("Canon", exifInfo.getMake());

    assertThat(exifInfo.getCameraInfo()).isEqualTo(new CameraInfo("0230101649", "Canon", "Canon EOS 7D", "S0027533"));
  }
}
