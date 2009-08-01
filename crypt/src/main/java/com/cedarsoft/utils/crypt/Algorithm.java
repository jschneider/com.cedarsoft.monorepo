package com.cedarsoft.utils.crypt;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an algorithm
 */
public enum Algorithm {
  MD5( "MD5" ),
  SHA1( "SHA-1", "SHA1" ),
  SHA256( "SHA-256", "SHA256" ),
  SHA512( "SHA-512", "SHA512" ),;

  @NotNull
  public static Algorithm getAlgorithm( @NotNull @NonNls String algorithmString ) {
    //First search for the exact match
    try {
      return Algorithm.valueOf( algorithmString );
    } catch ( IllegalArgumentException ignore ) {
    }

    //Now search for the alternative names
    for ( Algorithm algorithm : Algorithm.values() ) {
      if ( algorithm.alternativeNames.contains( algorithmString ) ) {
        return algorithm;
      }
    }
    throw new IllegalArgumentException( "No Alogirthm found for " + algorithmString );
  }

  @NotNull
  @NonNls
  private final List<String> alternativeNames = new ArrayList<String>();

  /**
   * Creates a new algorithm.
   * The first alternative name will be used to get the MessageDigest
   *
   * @param alternativeNames the alternative names
   */
  Algorithm( @NotNull @NonNls String... alternativeNames ) {
    if ( alternativeNames.length == 0 ) {
      throw new IllegalArgumentException( "Need at least one algorithm name" );
    }
    this.alternativeNames.addAll( Arrays.asList( alternativeNames ) );
  }

  /**
   * Returns a list containing all alternative names for the given algorithm
   *
   * @return the alternative names for the algorithm
   */
  @NotNull
  @NonNls
  public List<? extends String> getAlternativeNames() {
    return Collections.unmodifiableList( alternativeNames );
  }

  /**
   * Returns the Message Digest  algorithm
   *
   * @return the message digest
   */
  @NotNull
  public MessageDigest getMessageDigest() {
    try {
      return MessageDigest.getInstance( alternativeNames.get( 0 ) );
    } catch ( NoSuchAlgorithmException e ) {
      throw new RuntimeException( e );
    }
  }
}
