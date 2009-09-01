package com.cedarsoft;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.List;

/**
 *
 */
public class MockitoTest {
  @Test
  public void testTemplate() throws Exception {
    new MockitoTemplate() {
      @Mock
      private List<String> list;

      @Override
      protected void stub() throws Exception {
        when( list.size() ).thenReturn( 1 ).thenReturn( 2 );
        doThrow( new RuntimeException() ).when( list ).clear();
      }

      @Override
      protected void execute() throws Exception {
        list.add( "asdf" );
        list.add( "other" );

        assertEquals( 1, list.size() );
        assertEquals( 2, list.size() );

        try {
          list.clear();
          fail( "Where is the Exception" );
        } catch ( RuntimeException e ) {
        }
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( list, times( 2 ) ).size();

        verify( list ).add( "asdf" );
        verify( list ).add( "other" );
        verify( list ).clear();

        verify( list, never() ).iterator();
      }
    }.run();
  }

  @Test
  public void testIt() {
    List<String> list = mock( List.class );

    when( list.size() ).thenReturn( 1 ).thenReturn( 2 );
    doThrow( new RuntimeException() ).when( list ).clear();

    list.add( "asdf" );
    list.add( "other" );

    assertEquals( 1, list.size() );
    assertEquals( 2, list.size() );

    try {
      list.clear();
      fail( "Where is the Exception" );
    } catch ( RuntimeException e ) {
    }

    verify( list, times( 2 ) ).size();

    verify( list ).add( "asdf" );
    verify( list ).add( "other" );
    verify( list ).clear();

    verify( list, never() ).iterator();
    verifyNoMoreInteractions( list );

    //    Mockito.verify( list ).add( "asdf" );
    //    Mockito.verify( list ).add( "other" );
  }


  @Mock
  private List<String> mock;

  @Test
  public void testAnnotations() {
    MockitoAnnotations.initMocks( this );

    assertNotNull( mock );
    mock.add( "asdf" );

    verify( mock ).add( "asdf" );
  }
}
