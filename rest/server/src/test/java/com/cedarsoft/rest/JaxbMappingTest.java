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

package com.cedarsoft.rest;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.uri.UriBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.*;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class JaxbMappingTest {
  private JaxbMapping<MyObject, MyJaxbObject> mapping;

  @Before
  public void setUp() throws Exception {
    mapping = new JaxbMapping<MyObject, MyJaxbObject>() {
      @Override
      protected void setUris( @NotNull MyJaxbObject object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
      }

      @NotNull
      @Override
      protected MyJaxbObject createJaxbObject( @NotNull MyObject object ) {
        return new MyJaxbObject( object );
      }
    };
  }

  @Test
  public void testGetJaxbObject() throws Exception {
    MyObject myObject = new MyObject();

    MyJaxbObject myJaxbObject = mapping.getJaxbObject( new UriBuilderImpl(), myObject );
    assertSame( myObject, myJaxbObject.object );

    //test cache
    assertSame( myJaxbObject, mapping.getJaxbObject( new UriBuilderImpl(), myObject ) );
  }

  @Test
  public void testGetJaxbObjectNull() throws Exception {
    MyObject myObject = new MyObject();

    MyJaxbObject myJaxbObject = mapping.getJaxbObject( null, myObject );
    assertSame( myObject, myJaxbObject.object );

    //test cache
    assertSame( myJaxbObject, mapping.getJaxbObject( null, myObject ) );
  }

  @Test
  public void testGetJaxbObject2() throws Exception {
    MyObject myObject1 = new MyObject();
    MyObject myObject2 = new MyObject();

    List<MyJaxbObject> myJaxbObjects = mapping.getJaxbObjects( new UriBuilderImpl(), Lists.newArrayList( myObject1, myObject2 ) );
    assertSame( myObject1, myJaxbObjects.get( 0 ).object );
    assertSame( myObject2, myJaxbObjects.get( 1 ).object );

    //test cache
    assertSame( myJaxbObjects.get( 0 ), mapping.getJaxbObject( new UriBuilderImpl(), myObject1 ) );
    assertSame( myJaxbObjects.get( 1 ), mapping.getJaxbObject( new UriBuilderImpl(), myObject2 ) );
  }

  protected static class MyObject {

  }

  protected static class MyJaxbObject extends AbstractJaxbObject {
    private final MyObject object;

    protected MyJaxbObject( MyObject object ) {
      this.object = object;
    }
  }
}
