package com.cedarsoft.history;

import com.cedarsoft.EasyMockTemplate;
import org.easymock.classextension.EasyMock;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.List;

/**
 *
 */
public class ElementsCollectionTest {
  @Test
  public void testMultiRemove() {
    ElementsCollection<Integer> collection = new ElementsCollection<Integer>( 0, 1, 2, 3, 4, 5, 6, 7 );
    assertEquals( collection.size(), 8 );

    List<? extends Integer> removed = collection.removeElements( new ElementVisitor<Integer>( "" ) {
      @Override
      public boolean fits( @NotNull Integer element ) {
        return true;
      }
    } );

    assertEquals( removed.size(), 8 );
  }

  @Test
  public void testMultiRemoveListener() throws Exception {
    final ElementsCollection<Integer> collection = new ElementsCollection<Integer>( 0, 1, 2, 3, 4, 5, 6, 7 );
    assertEquals( collection.size(), 8 );

    final SingleElementsListener<Integer> listener = EasyMock.createMock( SingleElementsListener.class, SingleElementsListener.class.getMethod( "elementDeleted", ObservableCollection.class, Object.class, Integer.TYPE ) );

    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() {
        listener.elementDeleted( collection, 0, 0 );
        listener.elementDeleted( collection, 1, 0 );
        listener.elementDeleted( collection, 2, 0 );
        listener.elementDeleted( collection, 3, 0 );
        listener.elementDeleted( collection, 4, 0 );
        listener.elementDeleted( collection, 5, 0 );
        listener.elementDeleted( collection, 6, 0 );
        listener.elementDeleted( collection, 7, 0 );
      }

      @Override
      protected void codeToTest() {
        collection.addElementListener( listener );

        assertEquals( collection.removeElements( new ElementVisitor<Integer>( "" ) {
          @Override
          public boolean fits( @NotNull Integer element ) {
            return true;
          }
        } ).size(), 8 );
      }
    }.run();
  }
}
