package com.cedarsoft.gdao;

import com.cedarsoft.WriteableObjectAccess;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Access implementation that is based on a list.
 */
public class ListAccess<T> implements WriteableObjectAccess<T> {
  @NotNull
  private final List<T> list = new ArrayList<T>();

  public ListAccess() {
  }

  public ListAccess( @NotNull List<? extends T> entries ) {
    list.addAll( entries );
  }

  public void remove( @NotNull T element ) {
    list.remove( element );
  }

  public void add( @NotNull T element ) {
    list.add( element );
  }

  public void setElements( @NotNull List<? extends T> elements ) {
    list.clear();
    list.addAll( elements );
  }

  @NotNull
  public List<? extends T> getElements() {
    return Collections.unmodifiableList( list );
  }
}
