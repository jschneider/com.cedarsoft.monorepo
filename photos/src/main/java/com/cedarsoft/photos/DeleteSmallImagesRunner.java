package com.cedarsoft.photos;

import com.cedarsoft.photos.di.Modules;
import com.cedarsoft.photos.tools.exif.ExifHelper;
import com.cedarsoft.photos.tools.exif.ExifInfo;
import com.cedarsoft.photos.tools.imagemagick.Identify;
import com.cedarsoft.photos.tools.imagemagick.ImageInformation;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

/**
 * Finds small images
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DeleteSmallImagesRunner {
  public static void main(String[] args) throws IOException {
    Injector injector = Guice.createInjector(Modules.getModules());

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    Identify identify = injector.getInstance(Identify.class);
    ExifHelper exifHelper = injector.getInstance(ExifHelper.class);

    imageFinder.find((storage, dataFile, hash) -> {
      try {
        System.out.print(".");
        try {
          ExifInfo exifInfo = exifHelper.getExifInfo(hash);
          if (LinkByDateCreator.isRaw(exifInfo.getFileTypeExtension())) {
            return;
          }
        } catch (ExifHelper.NoExifInfoFoundException ignore) {
          System.out.println("No exif found for <" + hash + ">");
          return;
        }

        ImageInformation imageInformation = identify.getImageInformation(dataFile);
        if ((imageInformation.getResolution().getWidth() < 2000) && (imageInformation.getResolution().getHeight() < 2000)) {
          System.out.println("\n--> Small Image: " + imageInformation.getResolution() + " - " + dataFile.getAbsolutePath());

          storage.delete(hash);
        }
      } catch (Exception e) {
        System.err.println("Problem when checking " + dataFile.getAbsolutePath());
        e.printStackTrace();
        System.exit(1);
      }
    });
  }
}
