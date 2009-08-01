package com.cedarsoft.history;

import ca.odell.glazedlists.GlazedLists;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StopWatch;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks the performance of the glazed lists
 */
public class GlazedPerformanceTest {
  @Test
  public void testTiming() {
    for ( int i = 0; i < 5; i++ ) {
      checkPerformance( new ArrayList<String>() );
      checkPerformance( GlazedLists.<String>eventList( new ArrayList<String>() ) );
    }

    StopWatch watch0 = checkPerformance( new ArrayList<String>() );
    System.out.println( watch0.prettyPrint() );
    StopWatch watch1 = checkPerformance( GlazedLists.<String>eventList( new ArrayList<String>() ) );
    System.out.println( watch1.prettyPrint() );

    System.out.println( "Delta: " + ( watch1.getTotalTimeMillis() - watch0.getTotalTimeMillis() ) );
  }

  private static StopWatch checkPerformance( @NotNull List<String> list ) {
    StopWatch stopWatch = new StopWatch();

    stopWatch.start( "adding to " + list.getClass().getName() );
    for ( int i = 0; i < 100000; i++ ) {
      list.add( String.valueOf( i ) );
    }
    stopWatch.stop();

    stopWatch.start( "iterating through " + list.getClass().getName() );
    for ( String currentEntry : list ) {
      assertNotNull( currentEntry );
    }
    stopWatch.stop();

    assertNotNull( stopWatch );
    return stopWatch;
  }
}
