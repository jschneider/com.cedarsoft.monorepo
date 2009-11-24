package com.cedarsoft.event;

import org.jetbrains.annotations.Nullable;

/**
 * Provides the context for an event
 */
public interface EventContextProvider {
  @Nullable
  Object getContext();
}
