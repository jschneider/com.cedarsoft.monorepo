package com.cedarsoft.crypt;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents a signature
 */
public class Signature {
  /**
   * Null signature
   */
  @NotNull
  public static final Signature NULL = new Signature( new byte[0] );

  @NotNull
  private final byte[] bytes;

  /**
   * Creates a new signature
   *
   * @param bytes the bytes
   */
  public Signature( @NotNull byte[] bytes ) {
    this.bytes = bytes.clone();
  }

  /**
   * Returns the byte array representing the signature
   *
   * @return the byte array
   */
  @NotNull
  public byte[] getBytes() {
    return bytes.clone();
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Signature ) ) return false;

    Signature signature = ( Signature ) o;

    if ( !Arrays.equals( bytes, signature.bytes ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode( bytes );
  }
}