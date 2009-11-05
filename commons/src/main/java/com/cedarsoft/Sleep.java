package com.cedarsoft;

/**
 *
 */
public class Sleep {
  private Sleep() {
  }

  public static void forever() {
    now( Long.MAX_VALUE );
  }

  public static void now( long millis ) {
    try {
      Thread.sleep( millis );
    } catch ( InterruptedException e ) {
      throw new RuntimeException( e );
    }
  }
}
