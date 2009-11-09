package com.cedarsoft.gdao;

import com.cedarsoft.CommitableObjectAccess;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.List;

/**
 *
 */
public class CommitableListAccess<T> extends ListAccess<T> implements CommitableObjectAccess<T> {
  public CommitableListAccess() {
  }

  public CommitableListAccess( @NotNull List<? extends T> entries ) {
    super( entries );
  }

  @Override
  public void commit( @NotNull T element ) {
  }
}
