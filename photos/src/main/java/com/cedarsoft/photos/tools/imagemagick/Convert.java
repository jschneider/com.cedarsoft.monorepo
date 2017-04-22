package com.cedarsoft.photos.tools.imagemagick;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.image.Resolution;
import com.cedarsoft.io.FileOutputStreamWithMove;
import com.google.common.base.Joiner;
import org.im4java.core.CommandException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;
import org.im4java.core.ImageCommand;
import org.im4java.process.Pipe;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Convert {
  /**
   * Creates a thumbnail
   */
  @NonUiThread
  public void createThumbnail(@Nonnull File imageFile, @Nonnull File targetFile, @Nonnull Resolution maxResolution, @Nonnull String inTypeMagick) throws IOException {
    try (FileInputStream in = new FileInputStream(imageFile); FileOutputStreamWithMove out = new FileOutputStreamWithMove(targetFile)) {
      createThumbnail(in, out, maxResolution.getWidth(), maxResolution.getHeight(), inTypeMagick);
    }
  }

  protected void createThumbnail(@Nonnull InputStream in, @Nonnull OutputStream out, int maxWidth, int maxHeight, @Nonnull String inTypeMagick) throws IOException {
    IMOps operation = new IMOperation();
    operation.addImage(inTypeMagick + ":-");
    operation.resize(maxWidth, maxHeight);
    operation.addImage("jpeg:-");

    ImageCommand convertCmd = new ConvertCmd();
    convertCmd.setInputProvider(new Pipe(in, null));
    convertCmd.setOutputConsumer(new Pipe(null, out));

    try {
      convertCmd.run(operation);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (CommandException e) {
      throw new IOException("Command failed due to <" + Joiner.on(" ").join(e.getErrorText()) + ">", e);
    } catch (IM4JavaException e) {
      throw new IOException("Conversion failed due to: " + e.getMessage(), e);
    }
  }
}
