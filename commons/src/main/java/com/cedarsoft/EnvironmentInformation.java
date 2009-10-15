package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 */
public class EnvironmentInformation {
  private EnvironmentInformation() {
  }

  @NotNull
  @NonNls
  public static String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch ( UnknownHostException e ) {
      throw new RuntimeException( e );
    }
  }

  @NotNull
  @NonNls
  public static String getUserName() {
    return System.getProperty( "user.name" );
  }
}
