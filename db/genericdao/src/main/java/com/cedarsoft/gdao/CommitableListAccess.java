package com.cedarsoft.gdao;

import com.cedarsoft.CommitableObjectAccess;
import org.jetbrains.annotations.NotNull;

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

  public void commit( @NotNull T element ) {
  }
}
