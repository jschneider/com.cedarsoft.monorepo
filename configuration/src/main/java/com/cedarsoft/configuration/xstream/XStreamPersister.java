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

package com.cedarsoft.configuration.xstream;

import com.cedarsoft.configuration.ConfigurationManager;
import com.cedarsoft.configuration.ConfigurationPersister;
import com.thoughtworks.xstream.XStream;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Persists a configuration using XStream
 */
public class XStreamPersister implements ConfigurationPersister {
  @NonNls
  private static final String XML_HEADER_BEGIN = "<?xml version=\"1.0\"";
  @NonNls
  private static final String XML_ENCODING_BEGIN = " encoding=\"";
  @NonNls
  private static final String XML_ENCODING_SUFFIX = "\"";
  @NonNls
  private static final String XML_HEADER_SUFFIX = "?>";

  @NotNull
  private final XStream xStream;
  @NotNull
  private static final Charset DEFAULT_CHARSET = Charset.forName( "UTF-8" );

  /**
   * Creates a new persister
   */
  public XStreamPersister() {
    this.xStream = new XStream();
    xStream.alias( "configurations", Configurations.class );
    xStream.addImplicitCollection( Configurations.class, "configurations" );
  }

  /**
   * Persists the given configuration manager
   *
   * @param configurationManager the configuration manager
   * @param out                  the output stream
   * @throws IOException
   */
  @Override
  public void persist( @NotNull ConfigurationManager configurationManager, @NotNull Writer out ) throws IOException {
    persist( configurationManager, out, null );
  }

  /**
   * @param configurationManager
   * @param out
   * @param encoding
   * @throws IOException
   */
  @Override
  public void persist( @NotNull ConfigurationManager configurationManager, @NotNull Writer out, @Nullable @NonNls String encoding ) throws IOException {
    persist( configurationManager.getConfigurations(), out, encoding );
  }

  /**
   * Persists the given configurations
   *
   * @param configurations the configurations that are persisted
   * @param out            the writer
   * @throws IOException
   */
  @Override
  public void persist( @NotNull List<?> configurations, @NotNull Writer out ) throws IOException {
    persist( configurations, out, null );
  }

  @Override
  public void persist( @NotNull List<?> configurations, @NotNull Writer out, @Nullable @NonNls String encoding ) throws IOException {
    out.write( createXmlHeader( encoding ) );
    out.write( "\n" );
    xStream.toXML( new Configurations( configurations ), out );
  }

  @NotNull
  @NonNls
  protected String createXmlHeader( @Nullable @NonNls String encoding ) {
    if ( encoding == null ) {
      return XML_HEADER_BEGIN + XML_HEADER_SUFFIX;
    } else {
      return XML_HEADER_BEGIN + XML_ENCODING_BEGIN + encoding + XML_ENCODING_SUFFIX + XML_HEADER_SUFFIX;
    }
  }

  /**
   * Persists the configurations for the given manager into a string
   *
   * @param manager the manager
   * @return the persisted manager
   *
   * @throws IOException
   */
  @Deprecated
  @Override
  @NotNull
  @NonNls
  public String persist( @NotNull ConfigurationManager manager ) throws IOException {
    StringWriter out = new StringWriter();
    persist( manager.getConfigurations(), out );
    return out.toString();
  }

  /**
   * Loads the configurations
   *
   * @param serialized the serialized configurations
   * @return the deserialized configurations
   */
  @Override
  @NotNull
  public List<?> load( @NotNull @NonNls String serialized ) throws IOException {
    StringReader reader = new StringReader( serialized );
    return load( reader );
  }

  @Override
  @NotNull
  public List<?> load( @NotNull File file ) throws IOException {
    BufferedInputStream in = new BufferedInputStream( new FileInputStream( file ) );
    try {
      return load( in, DEFAULT_CHARSET );
    } finally {
      in.close();
    }
  }

  @Override
  @NotNull
  public List<?> load( @NotNull BufferedInputStream in, @NotNull Charset charset ) throws IOException {
    //noinspection IOResourceOpenedButNotSafelyClosed
    return load( ( Reader ) new InputStreamReader( in, charset ) );
  }

  @Override
  @NotNull
  public List<?> load( @NotNull Reader in ) throws IOException {
    //noinspection IOResourceOpenedButNotSafelyClosed
    BufferedReader br = new BufferedReader( in );
    String firstLine = br.readLine().trim();//xml

    if ( !firstLine.startsWith( XML_HEADER_BEGIN ) || !firstLine.endsWith( XML_HEADER_SUFFIX ) ) {
      throw new IllegalStateException( "Invalid start of file. Was <" + firstLine + "> but exepcted something like <" + XML_HEADER_BEGIN + XML_HEADER_SUFFIX + '>' );
    }

    return ( ( Configurations ) xStream.fromXML( br ) ).getConfigurations();
  }

  @Override
  public void persist( @NotNull ConfigurationManager manager, @NotNull File file ) throws IOException {
    OutputStream out = new BufferedOutputStream( new FileOutputStream( file ) );
    try {
      persist( manager, out, DEFAULT_CHARSET );
    } finally {
      out.close();
    }
  }

  @Override
  public void persist( @NotNull ConfigurationManager manager, @NotNull OutputStream out, @NotNull Charset charset ) throws IOException {
    //noinspection IOResourceOpenedButNotSafelyClosed
    Writer writer = new OutputStreamWriter( out, charset );
    persist( manager, writer, charset.name() );
    writer.flush();
  }


  private static class Configurations {
    @NotNull
    private final List<Object> configurations = new ArrayList<Object>();

    private Configurations( @NotNull List<?> configurations ) {
      this.configurations.addAll( configurations );
    }

    @NotNull
    public List<?> getConfigurations() {
      return Collections.unmodifiableList( configurations );
    }
  }
}