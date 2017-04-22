package com.cedarsoft.photos.di;

import com.cedarsoft.photos.ImageStorage;
import com.cedarsoft.photos.LinkByDateCreator;
import com.cedarsoft.photos.tools.exif.ExifExtractor;
import com.cedarsoft.photos.tools.exif.ExifTool;
import com.cedarsoft.photos.tools.imagemagick.Identify;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import java.io.File;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class StorageModule extends AbstractModule {
  @Override
  protected void configure() {
    File storageBaseDir = new File("/media/mule/data/media/photos/backend");
    File deletedBaseDir = new File("/media/mule/data/media/photos/deleted");
    bind(ImageStorage.class).toInstance(new ImageStorage(storageBaseDir, deletedBaseDir));
  }

  @Singleton
  @Provides
  @Nonnull
  public ExifTool provideExifTool() {
    File bin = new File("/usr/bin/exiftool");
    if (!bin.exists()) {
      throw new IllegalStateException("No exiftool installed.");
    }
    return new ExifTool(bin);
  }

  @Singleton
  @Provides
  @Nonnull
  public Identify providesIdentify() {
    File bin = new File("/usr/bin/identify");
    if (!bin.exists()) {
      throw new IllegalStateException("No identify installed.");
    }
    return new Identify(bin);
  }

  @Provides
  @Nonnull
  @Singleton
  public LinkByDateCreator provideLinkByDateCreator(@Nonnull ExifExtractor exifExtractor) {
    File byDateBaseDir = new File("/media/mule/data/media/photos/by-date");
    return new LinkByDateCreator(byDateBaseDir, exifExtractor);
  }
}
