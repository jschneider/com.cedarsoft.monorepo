/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.photos.tools;

import it.neckar.open.execution.OutputRedirector;

import javax.annotation.concurrent.Immutable;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for command line tools
 *
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
        throw new IOException("Conversion failed due to: " + IOUtils.toString(process.getErrorStream(), Charset.defaultCharset()));
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
