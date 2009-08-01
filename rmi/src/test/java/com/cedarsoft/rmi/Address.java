package com.cedarsoft.rmi;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 *
 */
public class Address implements Serializable {
  private static final long serialVersionUID = -6801368372333688805L;

  private Long id;

  @NotNull
  @NonNls
  private String street1 = "";
  @NotNull
  @NonNls
  private String street2 = "";

  public Address() {
  }

  public Address( @NotNull @NonNls String street1, @NotNull @NonNls String street2 ) {
    this.street1 = street1;
    this.street2 = street2;
  }

  @NotNull
  public String getStreet1() {
    return street1;
  }

  public void setStreet1( @NotNull String street1 ) {
    this.street1 = street1;
  }

  @NotNull
  public String getStreet2() {
    return street2;
  }

  public void setStreet2( @NotNull String street2 ) {
    this.street2 = street2;
  }
}
