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