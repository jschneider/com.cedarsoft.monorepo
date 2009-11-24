package com.cedarsoft.app;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class PasswordUtils {
  private PasswordUtils() {
  }

  @NotNull
  public static byte[] calculateMD5Hash( @NotNull @NonNls String password ) {
    byte[] bytes = password.getBytes();
    try {
      MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
      return messageDigest.digest( bytes );
    } catch ( NoSuchAlgorithmException e ) {
      throw new RuntimeException( e );
    }
  }

  public static boolean hasExpectedHash( @NotNull @NonNls String password, @Nullable byte[] expectedHash ) {
    if ( expectedHash == null ) {
      return false;
    }

    byte[] actual = calculateMD5Hash( password );
    try {
      validatePasswordHash( expectedHash, actual );
      return true;
    } catch ( InvalidPasswordException ignore ) {
      return false;
    }
  }

  public static void validatePasswordHash( @Nullable byte[] expected, @Nullable byte[] actual ) throws InvalidPasswordException {
    if ( expected == null || actual == null ) {
      throw new InvalidPasswordException();
    }
    if ( actual.length != expected.length ) {
      throw new InvalidPasswordException();
    }

    for ( int i = 0; i < actual.length; i++ ) {
      byte actualByte = actual[i];
      byte expectedByte = expected[i];

      if ( actualByte != expectedByte ) {
        throw new InvalidPasswordException();
      }
    }
  }
}
