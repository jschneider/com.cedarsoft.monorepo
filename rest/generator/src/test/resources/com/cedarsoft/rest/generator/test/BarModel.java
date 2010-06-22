package com.cedarsoft.rest.generator.test;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class BarModel {
  private final int daInt;
  private final String daString;

  private final List<String> stringList = new ArrayList<String>();
  private final List<? extends String> wildStringList = new ArrayList<String>();
  private final Set<? extends String> set = new HashSet<String>();

  public BarModel( int daInt, String daString, List<? extends String> wildStringList, List<? extends String> stringList, @NotNull Set<? extends String> set ) {
    this.daInt = daInt;
    this.daString = daString;
  }

  public int getDaInt() {
    return daInt;
  }

  public Set<? extends String> getSet() {
    return Collections.unmodifiableSet( set );
  }

  public String getDaString() {
    return daString;
  }

  public List<? extends String> getStringList() {
    return Collections.unmodifiableList( stringList );
  }

  public List<? extends String> getWildStringList() {
    return Collections.unmodifiableList( wildStringList );
  }
}
