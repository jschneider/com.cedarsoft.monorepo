package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.lang.Override;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p/>
 * Date: Apr 27, 2007<br>
 * Time: 3:46:29 PM<br>
 */
public class TypeRegistryTest {
  private TypeRegistry<ChildDetector<?, ?>> registry;

  @BeforeMethod
  protected void setUp() throws Exception {
    registry = new TypeRegistry<ChildDetector<?, ?>>();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testInterfaces() {
    registry.addElement( String.class, new AbstractChildDetector<String, Object>() {
      @Override
      @NotNull
      public List<? extends Object> findChildren( @NotNull String parent ) {
        return Collections.singletonList( "String" );
      }
    } );

    registry.addElement( Integer.class, new AbstractChildDetector<Integer, Object>() {
      @Override
      @NotNull
      public List<? extends Object> findChildren( @NotNull Integer parent ) {
        return Collections.singletonList( "Integer" );
      }
    } );

    assertEquals( "String", ( ( ChildDetector<String, Object> ) registry.getElement( String.class ) ).findChildren( "asdf" ).get( 0 ) );
    assertEquals( "Integer", ( ( ChildDetector<Integer, Object> ) registry.getElement( Integer.class ) ).findChildren( 5 ).get( 0 ) );
  }

  @Test
  public void testSuperTypes() {
    registry.addElement( Collection.class, new AbstractChildDetector<Object, Object>() {
      @Override
      @NotNull
      public List<? extends Object> findChildren( @NotNull Object parent ) {
        return Collections.singletonList( "Collection" );
      }
    } );

    assertEquals( "Collection", ( ( ( ChildDetector<String, ?> ) registry.getElement( Collection.class ) ) ).findChildren( "asdf" ).get( 0 ) );
    assertEquals( "Collection", ( ( ( ChildDetector<String, ?> ) registry.getElement( List.class ) ) ).findChildren( "asdf" ).get( 0 ) );
    assertEquals( "Collection", ( ( ( ChildDetector<String, ?> ) registry.getElement( ArrayList.class ) ) ).findChildren( "asdf" ).get( 0 ) );
  }

  @Test
  public void testFindDefault() {
    registry.addElement( Object.class, new AbstractChildDetector<Object, Object>() {
      @Override
      @NotNull
      public List<? extends Object> findChildren( @NotNull Object parent ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertNotNull( registry.getElement( String.class ) );
  }

  @Test
  public void testRegistry() {
    registry.addElement( JComponent.class, new AbstractChildDetector<String, String>() {
      @Override
      @NotNull
      public List<? extends String> findChildren( @NotNull String parent ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertNotNull( registry.getElement( JLabel.class ) );
    assertNotNull( registry.getElement( JComponent.class ) );

    assertSame( registry.getElement( JLabel.class ), registry.getElement( JComponent.class ) );
  }

  @Test
  public void testRegistry2() {
    registry = new TypeRegistry<ChildDetector<?, ?>>( false );

    registry.addElement( JComponent.class, new AbstractChildDetector<String, String>() {
      @Override
      @NotNull
      public List<? extends String> findChildren( @NotNull String parent ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertNotNull( registry.getElement( JComponent.class ) );
    try {
      registry.getElement( JLabel.class );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }
}
