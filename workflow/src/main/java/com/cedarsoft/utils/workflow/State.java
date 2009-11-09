package com.cedarsoft.utils.workflow;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a state within the workflow
 */
public class State<T> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  private final List<TransitionDefinition<T>> transitionDefinitions = new ArrayList<TransitionDefinition<T>>();

  /**
   * Creates a new state
   *
   * @param id the id
   */
  public State( @NotNull @NonNls String id ) {
    this.id = id;
  }

  /**
   * Returns the id
   *
   * @return the id
   */
  @NotNull
  public String getId() {
    return id;
  }

  /**
   * Creates a new transition definition for the given target state and action
   *
   * @param targetState the target state
   * @param actions     the actions
   * @return the definition
   */
  @NotNull
  public TransitionDefinition<T> createTransition( @NotNull State<T> targetState, @NotNull List<? extends Action<T>> actions ) {
    lock.writeLock().lock();
    try {
      TransitionDefinition<T> transitionDefinition = new TransitionDefinition<T>( this, targetState, actions );
      transitionDefinitions.add( transitionDefinition );
      return transitionDefinition;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @NotNull
  public TransitionDefinition<T> createTransition( @NotNull State<T> targetState, @NotNull Action<T>... actions ) {
    return createTransition( targetState, Arrays.asList( actions ) );
  }

  /**
   * Returns the transitions for this state
   *
   * @return the transitions
   */
  @NotNull
  public List<? extends TransitionDefinition<T>> getTransitions() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( transitionDefinitions );
    } finally {
      lock.readLock().unlock();
    }
  }

  private class EmptyAction<T> implements Action<T> {
    @Override
    public void execute( @NotNull T object, @NotNull TransitionDefinition<T> definition ) {
    }
  }
}
