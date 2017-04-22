package com.cedarsoft.photos;

import com.cedarsoft.photos.di.Modules;
import com.cedarsoft.photos.tools.exif.ExifExtractor;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ExtractExifsRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("Extracting exif files");

    Injector injector = Guice.createInjector(Modules.getModules());
    ExifExtractor exifExtractor = injector.getInstance(ExifExtractor.class);

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    imageFinder.find((storage, dataFile, hash) -> {
      File dir = dataFile.getParentFile();
      File exifFile = new File(dir, "exif");
      if (exifFile.exists()) {
        return;
      }

      File tmpOut = new File(dir, "exif.tmp");
      try {
        dir.setWritable(true);
        try (FileInputStream in = new FileInputStream(dataFile); FileOutputStream out = new FileOutputStream(tmpOut)) {
          exifExtractor.extractDetailed(in, out);
        }

        //Rename after success
        tmpOut.renameTo(exifFile);
      } catch (IOException ignore) {
        System.out.println("Failed: " + dataFile.getAbsolutePath());
      } finally {
        tmpOut.delete();
        dir.setWritable(false);
      }
    });
  }
}
