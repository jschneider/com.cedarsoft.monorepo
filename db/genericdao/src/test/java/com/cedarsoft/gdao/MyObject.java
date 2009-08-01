package com.cedarsoft.gdao;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class MyObject {
  @NonNls
  private Long id;

  public MyObject() {
  }

  public MyObject( @NonNls @NotNull String name ) {
    this.name = name;
  }

  @NonNls
  @Nullable
  public Long getId() {
    return id;
  }

  @NotNull
  @NonNls
  private String name = "";

  @NotNull
  @NonNls
  public String getName() {
    return name;
  }


  public void setName( @NotNull @NonNls String name ) {
    this.name = name;
  }
}
