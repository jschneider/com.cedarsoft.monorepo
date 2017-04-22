package com.cedarsoft.photos;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.crypt.Hash;
import lombok.extern.java.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Log
public class ImageFinder {
  @Nonnull
  private final ImageStorage storage;

  @Inject
  public ImageFinder(@Nonnull ImageStorage storage) {
    this.storage = storage;
  }

  /**
   * Method that only "finds" the image with the given hash
   */
  @NonUiThread
  public void find(@Nonnull Hash hash, @Nonnull Consumer consumer) throws IOException {
    File dataFile = storage.getDataFile(hash);
    consumer.found(storage, dataFile, hash);
  }

  @NonUiThread
  public void find(@Nonnull Consumer consumer) throws IOException {
    @Nullable File[] firstPartHashDirs = storage.getBaseDir().listFiles();

    assert firstPartHashDirs != null;
    for (File firstPartHashDir : firstPartHashDirs) {
      if (!firstPartHashDir.isDirectory()) {
        LOG.warning("Unexpected file found: " + firstPartHashDir.getAbsolutePath());
        continue;
      }

      File[] dataDirs = firstPartHashDir.listFiles();
      assert dataDirs != null;
      for (File dataDir : dataDirs) {
        File dataFile = new File(dataDir, ImageStorage.DATA_FILE_NAME);
        if (!dataFile.exists()) {
          LOG.warning("Missing data file: <" + dataFile.getAbsolutePath() + ">");
          continue;
        }

        Hash hash = Hash.fromHex(ImageStorage.ALGORITHM, dataDir.getName());
        consumer.found(storage, dataFile, hash);
      }
    }
  }

  /**
   * Consumer for found images
   */
  @FunctionalInterface
  public interface Consumer {
    /**
     * Is called for each data file that has been found
     */
    @NonUiThread
    void found(@Nonnull ImageStorage storage, @Nonnull File dataFile, @Nonnull Hash hash) throws IOException;
  }
}
