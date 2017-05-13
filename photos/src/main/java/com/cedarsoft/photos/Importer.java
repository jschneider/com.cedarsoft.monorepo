package com.cedarsoft.photos;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.crypt.Hash;
import com.cedarsoft.crypt.HashCalculator;
import com.cedarsoft.io.LinkUtils;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

/**
 * Imports photos from a given directory.
 * Creates hard links
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Importer {
  @Nonnull
  private static final ImmutableSet<String> SUPPORTED_FILE_SUFFICIES = ImmutableSet.of("jpeg", "jpg", "cr2");
  @Nonnull
  private final ImageStorage imageStorage;

  @Inject
  public Importer(@Nonnull ImageStorage imageStorage) {
    this.imageStorage = imageStorage;
  }

  /**
   * Imports the given file
   */
  @NonUiThread
  public void importFile(@Nonnull File fileToImport, @Nonnull Listener listener) throws IOException {
    Hash hash = HashCalculator.calculate(ImageStorage.ALGORITHM, fileToImport);

    File targetFile = imageStorage.getDataFile(hash);
    if (targetFile.exists()) {
      listener.skipped(hash, fileToImport, targetFile);
      return;
    }

    File dir = targetFile.getParentFile();
    ImageStorage.ensureDirectoryExists(dir);

    //Set writable before
    dir.setWritable(true, true);
    try {
      //Create a hard link
      LinkUtils.createHardLink(fileToImport, targetFile);

      //Set the file to read only
      targetFile.setWritable(false);
      targetFile.setExecutable(false);
    } catch (LinkUtils.AlreadyExistsWithOtherTargetException e) {
      throw new IOException(e);
    } finally {
      //Set to read only
      dir.setWritable(false, false);
    }

    listener.imported(hash, fileToImport, targetFile);
  }

  /**
   * Imports all files within the given directory
   */
  @NonUiThread
  public void importDirectory(@Nonnull File directory, @Nonnull Listener listener) throws IOException {
    if (!directory.isDirectory()) {
      throw new FileNotFoundException("Not a directory <" + directory.getAbsolutePath() + ">");
    }

    if (directory.getName().equals(".@__thumb")) {
      //Skip thumbs dir
      return;
    }

    @Nullable File[] files = directory.listFiles();
    if (files == null) {
      throw new IllegalStateException("Could not list files in <" + directory.getAbsolutePath() + ">");
    }

    for (File file : files) {
      if (isSupported(file)) {
        importFile(file, listener);
      }

      //Import all files from the sub directories
      if (file.isDirectory()) {
        importDirectory(file, listener);
      }
    }
  }

  /**
   * Returns whether the given file is supported
   */
  private static boolean isSupported(@Nonnull File file) {
    String fileNameLowerCase = file.getName().toLowerCase();

    for (String supportedFileSuffix : SUPPORTED_FILE_SUFFICIES) {
      if (fileNameLowerCase.endsWith("." + supportedFileSuffix)) {
        return true;
      }
    }

    return false;
  }

  public interface Listener {
    /**
     * Is called if the file is skipped
     */
    void skipped(@Nonnull Hash hash, @Nonnull File fileToImport, @Nonnull File targetFile);

    /**
     * Is called if the file has been imported
     */
    void imported(@Nonnull Hash hash, @Nonnull File fileToImport, @Nonnull File targetFile);
  }
}
