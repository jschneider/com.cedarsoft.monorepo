package com.cedarsoft.photos.tools.imagemagick;

import com.cedarsoft.image.Resolution;

import javax.annotation.Nonnull;

/**
 * Contains the image information returned from imagemagick "identify" command
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImageInformation {
  @Nonnull
  private final String type;
  @Nonnull
  private final Resolution resolution;

  public ImageInformation(@Nonnull String type, int width, int height) {
    this(type, new Resolution(width, height));
  }

  public ImageInformation(@Nonnull String type, @Nonnull Resolution resolution) {
    this.type = type;
    this.resolution = resolution;
  }

  @Nonnull
  public String getType() {
    return type;
  }

  @Nonnull
  public Resolution getResolution() {
    return resolution;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    ImageInformation that = (ImageInformation) obj;

    if (!type.equals(that.type)) {
      return false;
    }
    return resolution.equals(that.resolution);

  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + resolution.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "ImageInformation{" +
      "type='" + type + '\'' +
      ", resolution=" + resolution +
      '}';
  }
}
