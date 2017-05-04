package com.cedarsoft.photos.tools;

import com.cedarsoft.execution.OutputRedirector;
import com.google.errorprone.annotations.Immutable;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for command line tools
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Immutable
public abstract class AbstractCommandLineTool {
  @SuppressWarnings("Immutable")
  @Nonnull
  private final File bin;

  protected AbstractCommandLineTool(@Nonnull File bin) throws CmdLineToolNotAvailableException {
    if (!bin.exists()) {
      throw new CmdLineToolNotAvailableException("binary not found " + bin.getAbsolutePath());
    }
    this.bin = bin;
  }

  public void run(@Nonnull String... args) throws IOException {
    run(null, null, args);
  }

  public void run(@Nullable InputStream in, @Nullable OutputStream out, @Nonnull String... args) throws IOException {
    List<String> commands = new ArrayList<>();
    commands.add(bin.getAbsolutePath());
    commands.addAll(Arrays.asList(args));

    ProcessBuilder builder = new ProcessBuilder(commands);
    Process process = builder.start();

    @Nullable Thread outputRedirectingThread = null;
    if (out != null) {
      outputRedirectingThread = new Thread(new OutputRedirector(process.getInputStream(), out), "output stream redirection thread");
      outputRedirectingThread.start();
    }

    if (in != null) {
      //Copy the content of in to the input stream
      IOUtils.copy(in, process.getOutputStream());
      process.getOutputStream().close();
    }

    try {
      int result = process.waitFor();
      if (outputRedirectingThread != null) {
        outputRedirectingThread.join();
      }
      if (result != 0) {
        throw new IOException("Conversion failed due to: " + IOUtils.toString(process.getErrorStream()));
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
