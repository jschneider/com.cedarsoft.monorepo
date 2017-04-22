package com.cedarsoft.photos.tools.exif;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts the exif information
 */
public class ExifExtractor {
  @Nonnull
  private final ExifTool exifTool;

  @Inject
  public ExifExtractor(@Nonnull ExifTool exifTool) {
    this.exifTool = exifTool;
  }

  public void extractHumanReadable(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-a", "-");
  }

  public void extractDetailed(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-a", "-t", "-D", "-s", "-");
  }

  public void extractHtml(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-a", "-t", "-h", "-");
  }

  public void extractSummary(@Nonnull InputStream source, @Nonnull OutputStream out) throws IOException {
    exifTool.run(source, out, "-canon", "-");
  }

  /**
   * Copies the exif data from the source file to the target file - but not the Orientation
   *
   * @param source               the source
   * @param target               the target
   * @param includingOrientation whether the orientation is included
   */
  @Deprecated //untested!
  public void copyExif(@Nonnull File source, @Nonnull File target, boolean includingOrientation) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    List<String> args = new ArrayList<>();
    args.add("-q");
    if (!includingOrientation) {
      args.add("-x");
      args.add("Orientation");
    }
    args.add("-P");
    args.add("-overwrite_original");
    args.add("-TagsFromFile");
    args.add(source.getAbsolutePath());
    args.add(target.getAbsolutePath());

    exifTool.run(null, out, args.toArray(new String[args.size()]));

    if (out.toByteArray().length > 0) {
      throw new IOException("Conversion failed due to " + new String(out.toByteArray()));
    }
  }

  @Nonnull
  public ExifInfo extractInfo(@Nonnull InputStream imageIn) throws IOException {
    ByteArrayOutputStream exifOut = new ByteArrayOutputStream();
    extractDetailed(imageIn, exifOut);
    return new ExifInfo(new ByteArrayInputStream(exifOut.toByteArray()));
  }

  @Nonnull
  public ExifInfo extractInfo(@Nonnull File imageFile) throws IOException {
    try (InputStream in = new BufferedInputStream(new FileInputStream(imageFile))) {
      return extractInfo(in);
    }
  }

}