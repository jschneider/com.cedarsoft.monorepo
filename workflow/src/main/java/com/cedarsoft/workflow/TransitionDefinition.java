package com.cedarsoft.workflow;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines a transition between two states.
 *
 * @paramt <T> the type of object that is used within the workflow
 */
public class TransitionDefinition<T> {
  @NotNull
  private final State<T> source;
  @NotNull
  private final State<T> target;
  @NotNull
  private final List<Action<T>> actions = new ArrayList<Action<T>>();

  public TransitionDefinition( @NotNull State<T> source, @NotNull State<T> target, @NotNull Action<T>... actions ) {
    this( source, target, Arrays.asList( actions ) );
  }

  /**
   * Creates a new transition definition between the given source and target
   *
   * @param source the source
   * @param target the target
   */
  public TransitionDefinition( @NotNull State<T> source, @NotNull State<T> target, @NotNull List<? extends Action<T>> actions ) {
    this.source = source;
    this.target = target;
    this.actions.addAll( actions );
  }

  @NotNull
  public State<T> getSource() {
    return source;
  }

  @NotNull
  public State<T> getTarget() {
    return target;
  }

  /**
   * Returns the actions
   *
   * @return the action (if set)
   */
  @NotNull
  public List<? extends Action<T>> getActions() {
    return Collections.unmodifiableList( actions );
  }

  @Deprecated
  @NotNull
  public Action<T> getFirstAction() {
    return actions.get( 0 );
  }
}
