package com.cedarsoft.crypt;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a hash value
 */
public class Hash implements Serializable {
  private static final long serialVersionUID = 5728176239480983210L;

  @NotNull
  @NonNls
  private final Algorithm algorithm;
  @NotNull
  private final byte[] value;

  public Hash( @NotNull Algorithm algorithm, @NotNull byte[] value ) {
    this.algorithm = algorithm;
    this.value = value.clone();
  }

  @NotNull
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  @NotNull
  public String getValueAsHex() {
    return new String( Hex.encodeHex( value ) );
  }

  @NotNull
  public byte[] getValue() {
    return value.clone();
  }

  @Override
  @NonNls
  public String toString() {
    return "Hash{" +
      "algorithm=" + algorithm +
      ", value=" + getValueAsHex() +
      '}';
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Hash hash = ( Hash ) o;

    if ( algorithm != hash.algorithm ) return false;
    if ( !Arrays.equals( value, hash.value ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = algorithm.hashCode();
    result = 31 * result + ( value != null ? Arrays.hashCode( value ) : 0 );
    return result;
  }

  /**
   * Creates a hash from the given hex value
   *
   * @param algorithm  the algorithm
   * @param valueAsHex the hex value
   * @return the hash
   */
  @NotNull
  public static Hash fromHex( @NotNull Algorithm algorithm, @NotNull String valueAsHex ) {
    try {
      return new Hash( algorithm, Hex.decodeHex( valueAsHex.toCharArray() ) );
    } catch ( DecoderException e ) {
      throw new IllegalArgumentException( "Invalid hex string <" + valueAsHex + ">", e );
    }
  }
}
