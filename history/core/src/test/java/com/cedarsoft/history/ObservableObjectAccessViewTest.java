package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ObservableObjectAccessViewTest {
  private ClusteredElementsCollection<Number> collection;
  private ObservableObjectAccessView<Integer> view;

  @BeforeMethod
  protected void setUp() throws Exception {
    collection = new ClusteredElementsCollection<Number>();
    view = new ObservableObjectAccessView<Integer>( collection, new ObservableObjectAccessView.TypeBridge<Integer>( Integer.class ) );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testSinglMof() {
    try {
      Collections.singletonList( "a" ).add( "b" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testInitial() {
    collection.add( 1 );
    collection.add( 2 );
    collection.add( 3 );

    view = new ObservableObjectAccessView<Integer>( collection, new ObservableObjectAccessView.TypeBridge<Integer>( Integer.class ) );
    assertEquals( 3, view.getElements().size() );
  }

  @Test
  public void testListener2() {
    final ClusteredElementsCollection<Integer> core = new ClusteredElementsCollection<Integer>();
    DelegatingClusteredObservableObjectAccess<Integer> access = new DelegatingClusteredObservableObjectAccess<Integer>() {
      @Override
      @NotNull
      public ClusteredObservableObjectAccess<Integer> getDelegate() {
        return core;
      }
    };

    ObservableObjectAccessView<Integer> view = new ObservableObjectAccessView<Integer>( access, new ObservableObjectAccessView.TypeBridge<Integer>( Integer.class ) );

    final List<Integer> deleted = new ArrayList<Integer>();
    final List<Integer> added = new ArrayList<Integer>();
    final List<Integer> changed = new ArrayList<Integer>();

    view.addElementListener( new SingleElementsListener<Integer>() {
      @Override
      public void elementDeleted( @NotNull ObservableCollection<? extends Integer> source, @NotNull Integer element, int index ) {
        deleted.add( element );
      }

      @Override
      public void elementAdded( @NotNull ObservableCollection<? extends Integer> source, @NotNull Integer element, int index ) {
        added.add( element );
      }

      @Override
      public void elementChanged( @NotNull ObservableCollection<? extends Integer> source, @NotNull Integer element, int index ) {
        changed.add( element );
      }
    } );


    assertEquals( 0, deleted.size() );
    assertEquals( 0, added.size() );
    assertEquals( 0, changed.size() );

    core.add( 4 );

    assertEquals( 1, added.size() );
  }

  @Test
  public void testListeners() {
    final List<Integer> deleted = new ArrayList<Integer>();
    final List<Integer> added = new ArrayList<Integer>();
    final List<Integer> changed = new ArrayList<Integer>();

    view.addElementListener( new SingleElementsListener<Integer>() {
      @Override
      public void elementDeleted( @NotNull ObservableCollection<? extends Integer> source, @NotNull Integer element, int index ) {
        deleted.add( element );
      }

      @Override
      public void elementAdded( @NotNull ObservableCollection<? extends Integer> source, @NotNull Integer element, int index ) {
        added.add( element );
      }

      @Override
      public void elementChanged( @NotNull ObservableCollection<? extends Integer> source, @NotNull Integer element, int index ) {
        changed.add( element );
      }
    } );

    assertEquals( 0, deleted.size() );
    assertEquals( 0, added.size() );
    assertEquals( 0, changed.size() );

    collection.add( 4L );
    collection.commit( 4L );
    collection.remove( 4L );

    assertEquals( 0, deleted.size() );
    assertEquals( 0, added.size() );
    assertEquals( 0, changed.size() );

    collection.add( 4 );
    assertEquals( 0, deleted.size() );
    assertEquals( 1, added.size() );
    assertEquals( 0, changed.size() );
    collection.commit( 4 );
    assertEquals( 0, deleted.size() );
    assertEquals( 1, added.size() );
    assertEquals( 1, changed.size() );
    collection.remove( 4 );
    assertEquals( 1, deleted.size() );
    assertEquals( 1, added.size() );
    assertEquals( 1, changed.size() );
  }

  @Test
  public void testIt() {
    collection.add( 5 );
    assertEquals( 1, collection.size() );
    assertEquals( 1, view.getElements().size() );

    collection.add( 4L );
    assertEquals( 2, collection.size() );
    assertEquals( 1, view.getElements().size() );

    collection.remove( 5 );
    assertEquals( 1, collection.size() );
    assertEquals( 0, view.getElements().size() );
  }
}
