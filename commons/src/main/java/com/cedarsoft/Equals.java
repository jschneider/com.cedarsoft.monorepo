package com.cedarsoft;

import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class Equals {
  private Equals() {
  }

  public static boolean isEqual( @Nullable Object first, @Nullable Object second ) {
    //noinspection ObjectEquality
    if ( first == second ) {
      return true;
    }

    if ( first != null ) {
      return first.equals( second );
    } else {
      return second.equals( first );
    }
  }

}
