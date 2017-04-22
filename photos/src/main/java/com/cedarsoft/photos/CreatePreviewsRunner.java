package com.cedarsoft.photos;

import com.cedarsoft.image.Resolution;
import com.cedarsoft.photos.di.Modules;
import com.cedarsoft.photos.tools.exif.ExifHelper;
import com.cedarsoft.photos.tools.exif.ExifInfo;
import com.cedarsoft.photos.tools.imagemagick.Convert;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CreatePreviewsRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("Creating preview files");

    Injector injector = Guice.createInjector(Modules.getModules());
    Convert convert = injector.getInstance(Convert.class);
    ExifHelper exifHelper = injector.getInstance(ExifHelper.class);

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    imageFinder.find((storage, dataFile, hash) -> {
      try {
        ExifInfo exifInfo = exifHelper.getExifInfo(hash);
        String extension = exifInfo.getFileTypeExtension();

        File dir = dataFile.getParentFile();
        dir.setWritable(true);
        try {
          File thumbsDir = new File(dir, "thumbs");
          thumbsDir.mkdir();

          createThumb(convert, dataFile, thumbsDir, new Resolution(1920, 1080), extension);
          createThumb(convert, dataFile, thumbsDir, new Resolution(260, 260), extension);
        } finally {
          dir.setWritable(false);
        }
      } catch (ExifHelper.NoExifInfoFoundException ignore) {
        System.out.println("--> No exif available for <" + dataFile.getAbsolutePath() + ">");
      }
    });
  }

  private static void createThumb(@Nonnull Convert convert, @Nonnull File dataFile, @Nonnull File thumbsDir, @Nonnull Resolution maxResolution, @Nonnull String inTypeMagick) throws IOException {
    File thumbFile = new File(thumbsDir, maxResolution.getWidth() + "x" + maxResolution.getHeight());
    if (thumbFile.exists()) {
      return;
    }

    convert.createThumbnail(dataFile, thumbFile, maxResolution, inTypeMagick);
  }
}
