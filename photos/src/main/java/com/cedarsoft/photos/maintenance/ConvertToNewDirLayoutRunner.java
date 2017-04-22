package com.cedarsoft.photos.maintenance;

import com.cedarsoft.photos.ImageStorage;
import com.cedarsoft.photos.di.Modules;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.io.IOException;

/**
 * Converts to new backend file format
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ConvertToNewDirLayoutRunner {
  public static void main(String[] args) throws IOException, InterruptedException {
    if (true) {
      throw new UnsupportedOperationException("Are you sure you know what you are doing? I don't think so. This main will *destroy* data");
    }

    Injector injector = Guice.createInjector(Modules.getModules());

    ImageStorage imageStorage = injector.getInstance(ImageStorage.class);
    File baseDir = imageStorage.getDeletedBaseDir();

    for (File firstPartDir : baseDir.listFiles()) {
      String firstPart = firstPartDir.getName();

      for (File leftoverDir : firstPartDir.listFiles()) {
        String leftOver = leftoverDir.getName();

        if (leftOver.length() == 64) {
          //Already converted
          continue;
        }

        if (leftOver.length() != 62) {
          throw new IllegalStateException("Invalid dir <" + leftoverDir.getAbsolutePath() + ">");
        }

        String completeHashAsString = firstPart + leftOver;
        if (completeHashAsString.length() != 64) {
          throw new IllegalStateException("Invalid complete hash <" + completeHashAsString + "> for <" + leftoverDir.getAbsolutePath() + ">");
        }

        //Now rename
        File targetDir = new File(leftoverDir.getParent(), completeHashAsString);
        System.out.println("--> Renaming " + leftoverDir.getAbsolutePath() + " to " + targetDir.getAbsolutePath());
        if (!leftoverDir.renameTo(targetDir)) {
          throw new IOException("Could not rename <" + leftoverDir.getAbsolutePath() + "> to <" + targetDir.getAbsolutePath() + ">");
        }
      }
    }
  }
}
