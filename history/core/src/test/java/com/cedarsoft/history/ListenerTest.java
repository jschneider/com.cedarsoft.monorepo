package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ListenerTest {
  @BeforeMethod
  protected void setUp() throws Exception {
  }

  @Test
  public void testListener() {
    DefaultHistory<DefaultHistoryEntry> history = new DefaultHistory<DefaultHistoryEntry>();

    final List<DefaultHistoryEntry> adds = new ArrayList<DefaultHistoryEntry>();
    final List<DefaultHistoryEntry> removes = new ArrayList<DefaultHistoryEntry>();

    history.addHistoryListener( new HistoryListener<DefaultHistoryEntry>() {
      @java.lang.Override
      public void entryAdded( @NotNull DefaultHistoryEntry entry ) {
        adds.add( entry );
      }

      @java.lang.Override
      public void entryChanged( @NotNull DefaultHistoryEntry entry ) {
      }

      @java.lang.Override
      public void entryRemoved( @NotNull DefaultHistoryEntry entry ) {
        removes.add( entry );
      }
    } );

    assertEquals( 0, adds.size() );
    history.addEntry( new DefaultHistoryEntry() );
    assertEquals( 1, adds.size() );

    assertEquals( 0, removes.size() );
    history.removeEntry( history.getEntry( history.getEntries().size() - 1 ) );
    assertEquals( 1, removes.size() );
  }

  @Test
  public void testListener2() {
    ContinuousEntriesInformation<ContinuousEntry> entriesInformation = new ContinuousEntriesInformation<ContinuousEntry>( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) );

    final List<ContinuousEntry> adds = new ArrayList<ContinuousEntry>();
    final List<ContinuousEntry> removes = new ArrayList<ContinuousEntry>();

    entriesInformation.addHistoryListener( new HistoryListener<ContinuousEntry>() {
      @java.lang.Override
      public void entryChanged( @NotNull ContinuousEntry entry ) {
      }

      @java.lang.Override
      public void entryAdded( @NotNull ContinuousEntry entry ) {
        adds.add( entry );
      }

      @java.lang.Override
      public void entryRemoved( @NotNull ContinuousEntry entry ) {
        removes.add( entry );
      }
    } );

    assertEquals( 0, adds.size() );
    entriesInformation.addEntry( new DefaultContinuousEntry( new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 1, adds.size() );

    assertEquals( 0, removes.size() );
    entriesInformation.removeLastEntry();
    assertEquals( 1, removes.size() );
  }

  @Test
  public void testListener3() {
    DiscreteHistory<DiscreteHistoryEntry> history = new DiscreteHistory<DiscreteHistoryEntry>();

    final List<DiscreteHistoryEntry> adds = new ArrayList<DiscreteHistoryEntry>();
    final List<DiscreteHistoryEntry> removes = new ArrayList<DiscreteHistoryEntry>();

    history.addHistoryListener( new HistoryListener<DiscreteHistoryEntry>() {
      @java.lang.Override
      public void entryAdded( @NotNull DiscreteHistoryEntry entry ) {
        adds.add( entry );
      }

      @java.lang.Override
      public void entryRemoved( @NotNull DiscreteHistoryEntry entry ) {
        removes.add( entry );
      }

      @java.lang.Override
      public void entryChanged( @NotNull DiscreteHistoryEntry entry ) {
      }
    } );

    assertEquals( 0, adds.size() );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 1, adds.size() );

    assertEquals( 0, removes.size() );
    history.removeEntry( history.getEntries().get( history.getEntries().size() - 1 ) );
    assertEquals( 1, removes.size() );
  }
}
