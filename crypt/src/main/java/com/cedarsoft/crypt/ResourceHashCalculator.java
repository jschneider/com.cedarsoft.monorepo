package com.cedarsoft.crypt;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Calculates the hashes of resources
 */
@Deprecated
public class ResourceHashCalculator {
  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull URL resource ) throws IOException {
    return HashCalculator.calculate( algorithm.getMessageDigest(), resource );
  }

  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull InputStream resource ) throws IOException {
    return HashCalculator.calculate( algorithm.getMessageDigest(), resource );
  }

  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull byte[] resource ) throws IOException {
    return HashCalculator.calculate( algorithm.getMessageDigest(), new ByteArrayInputStream( resource ) );
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
