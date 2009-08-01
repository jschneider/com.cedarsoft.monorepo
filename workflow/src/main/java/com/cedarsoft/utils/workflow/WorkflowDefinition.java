package com.cedarsoft.utils.workflow;

import org.jetbrains.annotations.NotNull;

/**
 * A workflow definition.
 * A workflow definition is used to create a specific workflow for a given object.
 */
public class WorkflowDefinition<T> {
  @NotNull
  private final State<T> initialState;

  public WorkflowDefinition( @NotNull State<T> initialState ) {
    this.initialState = initialState;
  }

  @NotNull
  public State<T> getInitialState() {
    return initialState;
  }

  @NotNull
  public Workflow<T> createWorkflow( @NotNull T object ) {
    return new Workflow<T>( object, this );
  }
}
