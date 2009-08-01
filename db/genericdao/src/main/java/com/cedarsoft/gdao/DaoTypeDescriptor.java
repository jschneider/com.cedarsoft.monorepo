package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes the type of a dao and provides the optional lock provider
 */
public class DaoTypeDescriptor<T> {
  @Nullable
  private final LockProvider<T> lockProvider;

  @NotNull
  private final Class<T> type;

  public DaoTypeDescriptor( @NotNull Class<T> type, @Nullable LockProvider<T> lockProvider ) {
    this.type = type;
    this.lockProvider = lockProvider;
  }

  @Nullable
  public LockProvider<T> getLockProvider() {
    return lockProvider;
  }

  @NotNull
  public Class<T> getType() {
    return type;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( !( o instanceof DaoTypeDescriptor ) ) {
      return false;
    }

    DaoTypeDescriptor that = ( DaoTypeDescriptor ) o;

    if ( !type.equals( that.type ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = lockProvider != null ? lockProvider.hashCode() : 0;
    result = 31 * result + type.hashCode();
    return result;
  }
}
