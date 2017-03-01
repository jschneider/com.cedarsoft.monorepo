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

package com.cedarsoft.serialization.serializers.jackson.registry;

import com.cedarsoft.exceptions.StillContainedException;
import com.cedarsoft.test.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 *
 */
public class FileBasedSerializedObjectsAccessTest {
  private FileBasedObjectsAccess access;

  @Before
  public void setUp() throws Exception {
    access = new FileBasedObjectsAccess( TestUtils.createEmptyTmpDir(), "xml" );
  }

  @After
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory( access.getBaseDir() );
  }

  @Test
  public void testIt() throws IOException {
    assertEquals( 0, access.getIds().size() );
    {
      OutputStream out = access.openOut( "id" );
      IOUtils.write( "asdf".getBytes(), out );
      out.close();
    }

    assertEquals( 1, access.getIds().size() );
    assertTrue( access.getIds().contains( "id" ) );

    {
      InputStream in = access.getInputStream( "id" );
      assertEquals( "asdf", IOUtils.toString( in ) );
      in.close();
    }
    InputStream in = new FileBasedObjectsAccess( access.getBaseDir(), "xml" ).getInputStream( "id" );
    assertEquals( "asdf", IOUtils.toString( in ) );
    in.close();
  }

  @Test
  public void testExists() throws IOException {
    assertEquals( 0, access.getIds().size() );
    {
      OutputStream out = access.openOut( "id" );
      IOUtils.write( "asdf".getBytes(), out );
      out.close();
    }

    try {
      access.openOut( "id" );
      fail( "Where is the Exception" );
    } catch ( StillContainedException e ) {
    }
  }
}
