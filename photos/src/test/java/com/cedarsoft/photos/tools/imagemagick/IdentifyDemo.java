package com.cedarsoft.photos.tools.imagemagick;

import com.cedarsoft.image.Resolution;
import org.junit.*;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class IdentifyDemo {
  @Test
  public void it() throws Exception {
    Identify identify = createIdentify();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    identify.run(null, out, "/media/mule/data/media/photos/backend/5e/f17e82c55be6077735e6c968cfdc107967b8c6ec9a3b512c8af36430728afe/data");
    assertThat(out.toString()).isEqualTo("/media/mule/data/media/photos/backend/5e/f17e82c55be6077735e6c968cfdc107967b8c6ec9a3b512c8af36430728afe/data JPEG 2048x1536 2048x1536+0+0 8-bit sRGB 1.595MB 0.000u 0:00.000\n");

    ImageInformation information = identify.getImageInformation(new File("/media/mule/data/media/photos/backend/5e/f17e82c55be6077735e6c968cfdc107967b8c6ec9a3b512c8af36430728afe/data"));
    assertThat(information.getType()).isEqualTo("JPEG");
    assertThat(information.getResolution()).isEqualTo(new Resolution(2048, 1536));
  }

  @Nonnull
  public static Identify createIdentify() {
    File bin = new File("/usr/bin/identify");
    if (!bin.exists()) {
      throw new AssertionError("Imagemagick not installed. Could not find identify");
    }
    return new Identify(bin);
  }
}