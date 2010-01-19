/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.configuration;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 */
public interface ConfigurationPersister {
  /**
   * Persists the configurations for the given configuration manager
   *
   * @param configurationManager the configuration manager
   * @param out                  the writer the configuration is written to
   * @throws IOException
   */
  void persist( @NotNull ConfigurationManager configurationManager, @NotNull Writer out ) throws IOException;

  /**
   * Persists the given configurations
   *
   * @param configurations the configurations
   * @param out            the writer the configurations are written to
   * @throws IOException
   */
  void persist( @NotNull List<?> configurations, @NotNull Writer out ) throws IOException;

  /**
   * Persiste the configurations of the given manager
   *
   * @param manager the manager the configurations are persisted of
   * @return the persisted configurations as string
   *
   * @throws IOException
   */
  @Deprecated
  @NotNull
  @NonNls
  String persist( @NotNull ConfigurationManager manager ) throws IOException;

  /**
   * Persists the state to the given file (in UTF-8)
   *
   * @param manager the manager
   * @param file    the file the state is persisted to
   */
  void persist( @NotNull ConfigurationManager manager, @NotNull File file ) throws IOException;

  void persist( @NotNull ConfigurationManager configurationManager, @NotNull Writer out, @Nullable @NonNls String encoding ) throws IOException;

  void persist( @NotNull List<?> configurations, @NotNull Writer out, @Nullable @NonNls String encoding ) throws IOException;

  void persist( @NotNull ConfigurationManager manager, @NotNull OutputStream out, @NotNull Charset charset ) throws IOException;

  @NotNull
  List<?> load( @NotNull @NonNls String serialized ) throws IOException;

  @NotNull
  List<?> load( @NotNull Reader in ) throws IOException;

  @NotNull
  List<?> load( @NotNull File file ) throws IOException;

  @NotNull
  List<?> load( @NotNull BufferedInputStream in, @NotNull Charset charset ) throws IOException;
}