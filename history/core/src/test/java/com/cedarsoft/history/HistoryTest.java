package com.cedarsoft.history;

import org.joda.time.LocalDate;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class HistoryTest {
  private LocalDate begin;
  private LocalDate end;
  private History<DefaultHistoryEntry> history;
  private LocalDate middle;

  @BeforeMethod
  protected void setUp() throws Exception {
    begin = new LocalDate( 2007, 04, 06 );
    end = new LocalDate( 2007, 8, 01 );
    middle = new LocalDate( 2007, 7, 01 );
    history = new DefaultHistory<DefaultHistoryEntry>();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testDelete() {
    assertEquals( 0, history.getEntries().size() );
    DefaultHistoryEntry first = new DefaultHistoryEntry( begin );
    history.addEntry( first );
    assertEquals( 1, history.getEntries().size() );
    history.removeEntry( first );
    assertEquals( 0, history.getEntries().size() );
    try {
      history.getLatestEntry();
      fail( "Where is the Exception" );
    } catch ( NoValidElementFoundException e ) {
    }
  }

  @Test
  public void testChange() {
    assertEquals( 0, history.getEntries().size() );

    DefaultHistoryEntry first = new DefaultHistoryEntry( begin );
    history.addEntry( first );
    DefaultHistoryEntry second = new DefaultHistoryEntry( middle );
    history.addEntry( second );
    DefaultHistoryEntry third = new DefaultHistoryEntry( end );
    history.addEntry( third );

    assertSame( third, history.getLatestEntry() );
  }

  @Test
  public void testIt() {
    assertEquals( 0, history.getEntries().size() );
    DefaultHistoryEntry first = new DefaultHistoryEntry( begin );
    history.addEntry( first );
    DefaultHistoryEntry second = new DefaultHistoryEntry( middle );
    history.addEntry( second );
    DefaultHistoryEntry third = new DefaultHistoryEntry( end );
    history.addEntry( third );

    assertEquals( 3, history.getEntries().size() );
    assertSame( third, history.getLatestEntry() );
  }

  @Test
  public void testWrongOrder() {
    assertEquals( 0, history.getEntries().size() );
    history.addEntry( new DefaultHistoryEntry( middle ) );
    history.addEntry( new DefaultHistoryEntry( begin ) );
    history.addEntry( new DefaultHistoryEntry( end ) );
    assertEquals( 3, history.getEntries().size() );
  }
}
