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
package com.cedarsoft.test.utils;

import com.google.common.io.ByteStreams;
import org.junit.*;
import sun.net.www.http.HttpClient;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class KeepAliveTest {
  @Rule
  public ThreadExtension threadExtension = new ThreadExtension();
  @Rule
  public CatchAllExceptionsExtension catchAllExceptionsExtension = new CatchAllExceptionsExtension();

  @Test
  public void testKeepAlive() throws Exception {
    try {
      HttpClient client = HttpClient.New( new URL( "http://www.google.de" ) );
      assertThat( client ).isNotNull();
    } catch ( ConnectException e ) {
      assertThat( e ).hasMessage( "Connection refused" );
    }
  }

  @Test
  public void testTestIt() throws Exception {
    URLConnection urlConnection = new URL( "http://www.google.de" ).openConnection();
    urlConnection.connect();
    assertThat( urlConnection ).isInstanceOf( HttpURLConnection.class );
    Object content = urlConnection.getContent();
    assertThat( content ).isInstanceOf( InputStream.class );

    InputStream inputStream = ( InputStream ) content;
    byte[] bytes = ByteStreams.toByteArray( inputStream );
    assertThat( bytes.length ).isGreaterThan( 10000 );

    assertThat( inputStream.available() ).isEqualTo( 0 );

    inputStream.close();
  }
}