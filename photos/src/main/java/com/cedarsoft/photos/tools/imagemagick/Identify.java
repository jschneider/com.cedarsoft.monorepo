package com.cedarsoft.photos.tools.imagemagick;

import com.cedarsoft.photos.tools.AbstractCommandLineTool;
import com.cedarsoft.photos.tools.CmdLineToolNotAvailableException;
import com.google.common.base.Splitter;
import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Identify extends AbstractCommandLineTool {
  public Identify(@Nonnull File bin) throws CmdLineToolNotAvailableException {
    super(bin);
  }

  @Nonnull
  public ImageInformation getImageInformation(@Nonnull File file) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    run(null, out, file.getAbsolutePath());

    String output = out.toString(StandardCharsets.UTF_8);
    List<String> parts = Splitter.on(' ').splitToList(output);

    if (parts.size() < 6) {
      throw new IllegalStateException("Could not parse output <" + output + ">");
    }

    String type = parts.get(1);
    String dimensionString = parts.get(2);

    String[] dimensionParts = dimensionString.split("x");
    if (dimensionParts.length != 2) {
      throw new IllegalStateException("Invalid dimension <" + dimensionString + "> in output <" + output + ">");
    }

    int width = Integer.parseInt(dimensionParts[0]);
    int height = Integer.parseInt(dimensionParts[1]);

    return new ImageInformation(type, width, height);
  }
}
