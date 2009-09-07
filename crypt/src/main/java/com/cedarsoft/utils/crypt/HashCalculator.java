package com.cedarsoft.utils.crypt;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

/**
 *
 */
public class HashCalculator {
  private HashCalculator() {
  }

  @NotNull
  public static Hash calculate( @NotNull Algorithm algorithm, @NotNull @NonNls String value ) {
    return calculate( algorithm.getMessageDigest(), value );
  }

  @NotNull
  public static Hash calculate( @NotNull MessageDigest messageDigest, @NotNull @NonNls String value ) {
    messageDigest.reset();
    messageDigest.update( value.getBytes() );

    byte[] digest = messageDigest.digest();
    return new Hash( Algorithm.getAlgorithm( messageDigest.getAlgorithm() ), digest );
  }

  @NotNull
  public static Hash calculate( @NotNull MessageDigest messageDigest, @NotNull URL resource ) throws IOException {
    InputStream in = null;
    try {
      in = resource.openStream();
      return calculate( messageDigest, in );
    } finally {
      if ( in != null ) {
        in.close();
      }
    }
  }

  @NotNull
  public static Hash calculate( @NotNull MessageDigest messageDigest, @NotNull InputStream resourceIn ) throws IOException {
    messageDigest.reset();

    byte[] cache = new byte[255];
    int k;
    while ( ( k = resourceIn.read( cache ) ) > -1 ) {
      messageDigest.update( cache, 0, k );
    }

    byte[] digest = messageDigest.digest();
    return new Hash( Algorithm.getAlgorithm( messageDigest.getAlgorithm() ), digest );
  }
}
