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

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 *
 */
public class FooMappingTest extends AbstractMappedJaxbTest<FooModel, Foo> {
  @NotNull
  @Override
  protected JaxbMapping<FooModel, Foo> createMapping() {
    return new FooMapping();
  }

  @NotNull
  @Override
  protected FooModel createModel() {
    return new FooModel( "Hello", Arrays.asList( "A", "B", "C" ), Arrays.asList( new FooModel.BarModel( 1 ), new FooModel.BarModel( 2 ) ) );
  }

  @NotNull
  @Override
  protected Class<Foo> getJaxbType() {
    return Foo.class;
  }

  @NotNull
  @Override
  protected String expectedXml() {
    return
      "<ns2:foo xmlns:ns2=\"test:foo\" href=\"test:daUri\">\n" +
        "  <daValue>Hello</daValue>\n" +
        "</ns2:foo>";
  }

  private static class FooMapping extends JaxbMapping<FooModel, Foo> {
    @Override
    protected void setUris( @NotNull Foo object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
      object.setHref( new URI( "test:daUri" ) );
    }

    @NotNull
    @Override
    protected Foo createJaxbObject( @NotNull FooModel object ) {
      Foo foo = new Foo();
      foo.setDaValue( object.getDaValue() );
      return foo;
    }
  }
}
