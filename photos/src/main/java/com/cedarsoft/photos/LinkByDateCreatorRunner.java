package com.cedarsoft.photos;

import com.cedarsoft.exceptions.NotFoundException;
import com.cedarsoft.photos.di.Modules;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LinkByDateCreatorRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("Creating links by date");

    Injector injector = Guice.createInjector(Modules.getModules());
    LinkByDateCreator linkByDateCreator = injector.getInstance(LinkByDateCreator.class);

    ImageFinder imageFinder = injector.getInstance(ImageFinder.class);
    imageFinder.find((storage, dataFile, hash) -> {
      try {
        File link = linkByDateCreator.createLink(dataFile, hash);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
