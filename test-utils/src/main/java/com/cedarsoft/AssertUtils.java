package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AssertUtils {
  private AssertUtils() {
  }

  public static void assertOne( @Nullable Object current, @NotNull Object... expectedAlternatives ) {
    List<AssertionError> failed = new ArrayList<AssertionError>();

    for ( Object expectedAlternative : expectedAlternatives ) {
      try {
        assertEquals( current, expectedAlternative );
        return; //Successfully
      } catch ( AssertionError e ) {
        failed.add( e );
      }
    }

    StringBuilder message = new StringBuilder();

    for ( AssertionError assertionError : failed ) {
      message.append( assertionError.getMessage() );
      message.append( "\n" );
    }

    throw new AssertionError( message.toString() );
  }
}
