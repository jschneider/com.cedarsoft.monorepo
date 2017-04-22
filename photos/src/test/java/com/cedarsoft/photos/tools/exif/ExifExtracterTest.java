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

import org.junit.*;
import org.junit.rules.*;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class ExifExtracterTest {
  private InputStream source;
  private URL sourceUrl;

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    sourceUrl = getClass().getResource("/img1.jpg");
    source = sourceUrl.openStream();
    assert source != null;
  }

  @Nonnull
  public static ExifExtractor create() {
    return new ExifExtractor(ExifToolTest.createExifTool());
  }

  @Test
  public void testIt() throws Exception {
    ExifExtractor extractor = create();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    extractor.extractDetailed(source, out);

    String content = new String(out.toByteArray());

    assertThat(content).contains("272\tModel\tCanon EOS 7D");
  }
}
