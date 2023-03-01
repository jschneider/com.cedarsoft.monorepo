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

import it.neckar.open.crypt.Hash;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Contains the report for imported files
 *
 */
public class ImportReport {
  private static final Logger LOG = Logger.getLogger(ImportReport.class.getName());

  @Nonnull
  private final List<? extends Hash> importedHashes;
  @Nonnull
  private final List<? extends Hash> alreadyExisting;
  @Nonnull
  private final List<? extends File> createdLinks;

  public ImportReport(@Nonnull List<? extends Hash> importedHashes, @Nonnull List<? extends Hash> alreadyExisting, @Nonnull List<? extends File> createdLinks) {
    this.importedHashes = ImmutableList.copyOf(importedHashes);
    this.alreadyExisting = ImmutableList.copyOf(alreadyExisting);
    this.createdLinks = ImmutableList.copyOf(createdLinks);
  }

  @Nonnull
  public List<? extends Hash> getImportedHashes() {
    //noinspection ReturnOfCollectionOrArrayField
    return importedHashes;
  }

  @Nonnull
  public List<? extends Hash> getAlreadyExisting() {
    //noinspection ReturnOfCollectionOrArrayField
    return alreadyExisting;
  }

  @Nonnull
  public List<? extends File> getCreatedLinks() {
    //noinspection ReturnOfCollectionOrArrayField
    return createdLinks;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ImportReport that = (ImportReport) obj;
    return Objects.equals(importedHashes, that.importedHashes) &&
      Objects.equals(alreadyExisting, that.alreadyExisting) &&
      Objects.equals(createdLinks, that.createdLinks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(importedHashes, alreadyExisting, createdLinks);
  }

  @Override
  public String toString() {
    return "ImportReport{" +
      "importedHashes=" + importedHashes +
      ", alreadyExisting=" + alreadyExisting +
      ", createdLinks=" + createdLinks +
      '}';
  }

  @Nonnull
  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    @Nonnull
    private final List<Hash> importedHashes = new ArrayList<>();
    @Nonnull
    private final List<Hash> alreadyExisting = new ArrayList<>();
    @Nonnull
    private final List<File> createdLinks = new ArrayList<>();

    private Builder() {
    }

    @Nonnull
    public Builder withAlreadyExisting(@Nonnull Hash existing) {
      alreadyExisting.add(existing);
      return this;
    }

    @Nonnull
    public Builder withCreatedLink(@Nonnull File link) {
      createdLinks.add(link);
      return this;
    }

    @Nonnull
    public Builder withImportedHash(@Nonnull Hash imported) {
      importedHashes.add(imported);
      return this;
    }

    @Nonnull
    public ImportReport build() {
      return new ImportReport(importedHashes, alreadyExisting, createdLinks);
    }
  }
}
