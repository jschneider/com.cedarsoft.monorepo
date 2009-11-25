package com.cedarsoft.rmi;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 *
 */
public class Customer implements Serializable {
  private static final long serialVersionUID = -3159228476750691008L;

  private Long id;
  @NotNull
  private final Address address;
  @NotNull
  @NonNls
  private String name = "";

  protected Customer() {
    address = null;
  }

  public Customer( @NotNull Address address ) {
    this.address = address;
  }

  @NotNull
  public Address getAddress() {
    return address;
  }

  @NotNull
  @NonNls
  public String getName() {
    return name;
  }

  public void setName( @NotNull @NonNls String name ) {
    this.name = name;
  }
}
