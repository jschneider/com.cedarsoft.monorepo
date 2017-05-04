package com.cedarsoft.photos;

import com.cedarsoft.crypt.Hash;
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
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
    private List<Hash> importedHashes = new ArrayList<>();
    @Nonnull
    private List<Hash> alreadyExisting = new ArrayList<>();
    @Nonnull
    private List<File> createdLinks = new ArrayList<>();

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
