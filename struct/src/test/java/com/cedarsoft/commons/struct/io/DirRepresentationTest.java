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

package com.cedarsoft.commons.struct.io;

import com.cedarsoft.CanceledException;
import com.cedarsoft.MockitoTemplate;
import com.cedarsoft.TestUtils;
import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.NodeFactory;
import com.cedarsoft.commons.struct.Path;
import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mock;
import org.junit.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 */
public class DirRepresentationTest {

  private Node root;

  @BeforeMethod
  public void setup() {
    root = new DefaultNode( "root" );

    DefaultNode a = new DefaultNode( "a" );
    root.addChild( a );
    a.addChild( new DefaultNode( "aa" ) );
    a.addChild( new DefaultNode( "ab" ) );

    root.addChild( new DefaultNode( "b" ) );
  }

  @Test
  public void testSubDirs() {
    File baseDir = TestUtils.createEmptyTmpDir();
    assertEquals( baseDir.list().length, 0 );

    {
      DirRepresenter representer = new DirRepresenter( root, false );
      representer.store( baseDir, null );

      assertTrue( new File( baseDir, "a/aa" ).isDirectory() );
      new File( baseDir, "a/aa/sub" ).mkdir();
    }


    Node root = new DefaultNode( "root" );
    assertEquals( root.getChildren().size(), 0 );

    DirRepresenter representer = new DirRepresenter( root, false );
    representer.parse( baseDir, new NodeFactory() {
      @Override
      @NotNull
      public Node createNode( @NotNull @NonNls String name, @NotNull Lookup context ) throws CanceledException {
        File file = context.lookupNonNull( File.class );
        assertEquals( file.getName(), name );

        assertNotNull( file );
        assertNotNull( context.lookup( Node.class ) );
        return new DefaultNode( name );
      }
    }, 2 );

    assertEquals( root.getChildren().size(), 2 );
    assertEquals( root.findChild( "a" ).findChild( "aa" ).getChildren().size(), 0 );
  }

  @Test
  public void testRootInvisible() {
    File baseDir = TestUtils.createEmptyTmpDir();
    assertEquals( baseDir.list().length, 0 );

    {
      DirRepresenter representer = new DirRepresenter( root, false );
      representer.store( baseDir, null );

      List<File> firstLevels = Arrays.asList( baseDir.listFiles() );
      Collections.sort( firstLevels );


      assertEquals( firstLevels.size(), 2 );

      assertTrue( new File( baseDir, "a/aa" ).isDirectory() );
      assertTrue( new File( baseDir, "a/ab" ).isDirectory() );
      assertTrue( new File( baseDir, "b" ).isDirectory() );
    }

    {
      Node root = new DefaultNode( "root" );
      assertEquals( root.getChildren().size(), 0 );

      DirRepresenter representer = new DirRepresenter( root, false );
      representer.parse( baseDir, new NodeFactory() {
        @Override
        @NotNull
        public Node createNode( @NotNull @NonNls String name, @NotNull Lookup context ) throws CanceledException {
          File file = context.lookupNonNull( File.class );
          assertEquals( file.getName(), name );

          assertNotNull( file );
          assertNotNull( context.lookup( Node.class ) );
          return new DefaultNode( name );
        }
      }, 99 );

      assertEquals( root.getChildren().size(), 2 );
      assertEquals( root.findChild( "a" ).getChildren().size(), 2 );
    }
  }

  @Test
  public void testRootVisible() {
    File baseDir = TestUtils.createEmptyTmpDir();
    assertEquals( baseDir.list().length, 0 );

    {
      DirRepresenter representer = new DirRepresenter( root, true );
      representer.store( baseDir, null );

      assertEquals( baseDir.listFiles().length, 1 );

      assertTrue( new File( baseDir, "root/a/aa" ).isDirectory() );
      assertTrue( new File( baseDir, "root/a/ab" ).isDirectory() );
      assertTrue( new File( baseDir, "root/b" ).isDirectory() );
    }

    {
      Node root = new DefaultNode( "root" );
      assertEquals( root.getChildren().size(), 0 );

      DirRepresenter representer = new DirRepresenter( root, true );
      representer.parse( baseDir, new NodeFactory() {
        @Override
        @NotNull
        public Node createNode( @NotNull @NonNls String name, @NotNull Lookup context ) throws CanceledException {
          File file = context.lookupNonNull( File.class );
          assertEquals( file.getName(), name );

          assertNotNull( context.lookup( Node.class ) );
          return new DefaultNode( name );
        }
      }, 99 );

      assertEquals( root.getChildren().size(), 2 );
      assertEquals( root.findChild( "a" ).getChildren().size(), 2 );
    }
  }

  @Test
  public void testCallbackCreation() throws Exception {
    final File baseDir = TestUtils.createEmptyTmpDir();

    assertEquals( baseDir.list().length, 0 );

    final DirRepresenter representer = new DirRepresenter( root, true );

    new MockitoTemplate() {
      @Mock
      private DirRepresenter.StoreCallback callback;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        representer.store( baseDir, callback );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( callback ).dirCreated( root, new Path( "root" ), new File( baseDir, "root" ) );
        verify( callback ).dirCreated( root.findChild( "a" ), Path.createPath( "root/a" ), new File( baseDir, "root/a" ) );
        verify( callback ).dirCreated( root.findChild( "b" ), Path.createPath( "root/b" ), new File( baseDir, "root/b" ) );
        verify( callback ).dirCreated( root.findChild( "a" ).findChild( "aa" ), Path.createPath( "root/a/aa" ), new File( baseDir, "root/a/aa" ) );
        verify( callback ).dirCreated( root.findChild( "a" ).findChild( "ab" ), Path.createPath( "root/a/ab" ), new File( baseDir, "root/a/ab" ) );
      }
    }.run();
  }
}
