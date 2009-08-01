package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event that represents the change of something
 */
public class ChangedEvent<T> {
  @NotNull
  private final T changedObject;
  @NotNull
  private final PropertiesPath propertiesPath;
  @Nullable
  private final Object context;

  public ChangedEvent( @NotNull T changedObject, @Nullable Object context, @NonNls @NotNull String... propertiesPath ) {
    if ( propertiesPath.length == 0 ) {
      throw new IllegalArgumentException( "Empty properties path" );
    }
    this.changedObject = changedObject;
    this.context = context;
    this.propertiesPath = new PropertiesPath( propertiesPath );
  }

  @NotNull
  public PropertiesPath getPropertiesPath() {
    return propertiesPath;
  }

  /**
   * The context object
   *
   * @return the context object
   */
  @Nullable
  public Object getContext() {
    return context;
  }

  /**
   * The changed object
   *
   * @return the changed object
   */
  @NotNull
  public T getChangedObject() {
    return changedObject;
  }

  @Deprecated
  @NotNull
  @NonNls
  public String getRootProperty() {
    return propertiesPath.getRootProperty();
  }
}
