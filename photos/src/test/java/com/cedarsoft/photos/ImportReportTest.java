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

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;
import com.google.common.collect.ImmutableList;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImportReportTest {
  private static final Logger LOG = Logger.getLogger(ImportReportTest.class.getName());

  @Test
  public void log() throws Exception {
    LOG.warning("asdf");
  }

  @Test
  public void basic() throws Exception {
    ImportReport importReport = new ImportReport(ImmutableList.of(), ImmutableList.of(), ImmutableList.of());
    assertThat(importReport.getAlreadyExisting()).isEmpty();
  }

  @Test
  public void builder() throws Exception {
    ImportReport importReport = ImportReport.builder()
      .withImportedHash(new Hash(Algorithm.SHA256, "asdf".getBytes(StandardCharsets.UTF_8)))
      .withAlreadyExisting(new Hash(Algorithm.SHA256, "2".getBytes(StandardCharsets.UTF_8)))
      .withCreatedLink(new File("asdf"))
      .build();

    assertThat(importReport.getAlreadyExisting()).hasSize(1);
    assertThat(importReport.getImportedHashes()).hasSize(1);
    assertThat(importReport.getCreatedLinks()).hasSize(1);
    assertThat(importReport.getCreatedLinks()).isInstanceOf(ImmutableList.class);

    assertThat(importReport.toString()).isEqualTo("ImportReport{importedHashes=[[SHA256: 61736466]], alreadyExisting=[[SHA256: 32]], createdLinks=[asdf]}");
  }
}
