package com.cedarsoft.photos;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImageStorage {
  @Nonnull
  public static final Algorithm ALGORITHM = Algorithm.SHA256;

  @Nonnull
  public static final String DATA_FILE_NAME = "data";
  @Nonnull
  private final File baseDir;
  @Nonnull
  private final File deletedBaseDir;

  public ImageStorage(@Nonnull File baseDir, @Nonnull File deletedBaseDir) {
    this.baseDir = baseDir;
    this.deletedBaseDir = deletedBaseDir;

    if (!baseDir.isDirectory()) {
      throw new IllegalArgumentException("Base dir does not exist <" + baseDir.getAbsolutePath() + ">");
    }
  }

  @Nonnull
  @NonUiThread
  File getDataFile(@Nonnull Hash hash) throws IOException {
    File dir = getDir(SplitHash.split(hash));
    return new File(dir, DATA_FILE_NAME);
  }

  /**
   * Returns the dir for the given hash
   */
  @Nonnull
  @NonUiThread
  public File getDir(@Nonnull Hash hash) throws IOException {
    return getDir(SplitHash.split(hash));
  }

  @NonUiThread
  @Nonnull
  private File getDir(@Nonnull SplitHash splitHash) {
    File firstPartDir = getFirstPartDir(splitHash.getFirstPart());
    return new File(firstPartDir, splitHash.getHashAsHex());
  }

  /**
   * Returns the dir for the first part
   */
  @Nonnull
  private File getFirstPartDir(@Nonnull String firstPart) {
    return new File(baseDir, firstPart);
  }

  @Nonnull
  public File getBaseDir() {
    return baseDir;
  }

  @Nonnull
  public File getDeletedBaseDir() {
    return deletedBaseDir;
  }

  /**
   * Returns the dir for the first part
   */
  @Nonnull
  private File getDeletedFirstPartDir(@Nonnull String firstPart) {
    return new File(deletedBaseDir, firstPart);
  }

  @NonUiThread
  @Nonnull
  private File getDeletedDir(@Nonnull SplitHash splitHash) {
    File firstPartDir = getDeletedFirstPartDir(splitHash.getFirstPart());
    return new File(firstPartDir, splitHash.getHashAsHex());
  }

  @Nonnull
  @NonUiThread
  File getDeletedDataFile(@Nonnull Hash hash) throws IOException {
    File dir = getDeletedDir(SplitHash.split(hash));
    return new File(dir, DATA_FILE_NAME);
  }

  public void delete(@Nonnull Hash hash) throws IOException {
    SplitHash splitHash = SplitHash.split(hash);
    File targetDir = getDeletedDir(splitHash);

    //Already deleted
    if (targetDir.exists()) {
      FileUtils.deleteDirectory(targetDir);
    }

    //Now move the original directory
    File dirToDelete = getDir(splitHash);
    dirToDelete.setWritable(true);
    FileUtils.moveDirectory(dirToDelete, targetDir);
  }

  public static void ensureDirectoryExists(@Nonnull File dir) throws IOException {
    if (!dir.isDirectory()) {
      if (!dir.mkdirs()) {
        throw new IOException("Could not create directory <" + dir.getAbsolutePath() + ">");
      }
    }
  }

}
