package com.cedarsoft.utils.workflow;

import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class Workflow<T> {
  @NotNull
  private final WorkflowDefinition<T> workflowDefinition;
  @NotNull
  private final T bean;

  @NotNull
  private State<T> currentState;

  @NotNull
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * Creates a workflow for the given bean
   *
   * @param bean               the bean the workflow is created for
   * @param workflowDefinition the definition
   */
  public Workflow( @NotNull T bean, @NotNull WorkflowDefinition<T> workflowDefinition ) {
    this.workflowDefinition = workflowDefinition;
    this.bean = bean;
    currentState = workflowDefinition.getInitialState();
  }

  /**
   * Returns the bean of that workflow
   *
   * @return the bean
   */
  @NotNull
  public T getBean() {
    return bean;
  }

  /**
   * Returns the workflow definition this workflow is based on
   *
   * @return the workflow definition
   */
  @NotNull
  public WorkflowDefinition<T> getWorkflowDefinition() {
    return workflowDefinition;
  }

  /**
   * Returns the current state
   *
   * @return the current state
   */
  @NotNull
  public State<T> getCurrentState() {
    lock.readLock().lock();
    try {
      return currentState;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the possible transitions.
   *
   * @return the possible transitions
   */
  @NotNull
  public List<? extends Transition<T>> getPossibleTransition() {
    List<Transition<T>> list = new ArrayList<Transition<T>>();
    for ( TransitionDefinition<T> definition : getCurrentState().getTransitions() ) {
      list.add( createTransition( definition ) );
    }
    return list;
  }

  /**
   * Returns the possible transition definitions
   *
   * @return the currently possible transition definitions
   */
  @NotNull
  @Deprecated
  public List<? extends TransitionDefinition<T>> getPossibleTransitionDefinitions() {
    return getCurrentState().getTransitions();
  }

  @NotNull
  private Transition<T> createTransition( @NotNull final TransitionDefinition<T> definition ) {
    return new Transition<T>() {
      @Override
      public void transit() {
        lock.writeLock().lock();
        try {
          //Now execute the actions
          for ( Action<T> action : definition.getActions() ) {
            action.execute( bean, definition );
          }
          currentState = definition.getTarget();
        } finally {
          lock.writeLock().unlock();
        }
      }
    };
  }
}
