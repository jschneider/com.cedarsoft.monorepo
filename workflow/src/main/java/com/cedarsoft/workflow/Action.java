package com.cedarsoft.workflow;

import org.jetbrains.annotations.NotNull;

/**
 * An action
 */
public interface Action<T> {
  /**
   * Executes the action
   *
   * @param object     the object
   * @param definition the definition of the transition
   */
  void execute( @NotNull T object, @NotNull TransitionDefinition<T> definition );
}
