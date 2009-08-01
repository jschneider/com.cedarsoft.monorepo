package com.cedarsoft.utils.crypt;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

/**
 * Calculates the hashs of resources
 */
public class ResourceHashCalculator {
  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull URL resource ) throws IOException {
    return calculate( algorithm.getMessageDigest(), resource );
  }

  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull InputStream resource ) throws IOException {
    return calculate( algorithm.getMessageDigest(), resource );
  }

  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull byte[] resource ) throws IOException {
    return calculate( algorithm.getMessageDigest(), new ByteArrayInputStream( resource ) );
  }

  @NotNull
  private static Hash calculate( @NotNull MessageDigest messageDigest, @NotNull URL resource ) throws IOException {
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
  private static Hash calculate( @NotNull MessageDigest messageDigest, @NotNull InputStream resourceIn ) throws IOException {
    messageDigest.reset();

    byte[] cache = new byte[255];
    int k;
    while ( ( k = resourceIn.read( cache ) ) > -1 ) {
      messageDigest.update( cache, 0, k );
    }

    byte[] digest = messageDigest.digest();
    return new Hash( Algorithm.getAlgorithm( messageDigest.getAlgorithm() ), digest );
  }

  @Deprecated
  @NotNull
  public Hash calculateSHA1( @NotNull InputStream resource ) throws IOException {
    return calculate( Algorithm.SHA1, resource );
  }

  @Deprecated
  @NotNull
  public Hash calculateMD5( @NotNull InputStream resource ) throws IOException {
    return calculate( Algorithm.MD5, resource );
  }
}
