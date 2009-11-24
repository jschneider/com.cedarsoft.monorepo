package com.cedarsoft;

import com.cedarsoft.DeletionProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Support for deletion processors.
 */
public class DeletionProcessorsSupport<T> {
  @NotNull
  private final List<DeletionProcessor<T>> deletionProcessors = new ArrayList<DeletionProcessor<T>>();

  /**
   * Sets the deletion processors
   *
   * @param deletionProcessors the processors
   */
  public void setDeletionProcessors( @NotNull List<? extends DeletionProcessor<T>> deletionProcessors ) {
    this.deletionProcessors.clear();
    this.deletionProcessors.addAll( deletionProcessors );
  }

  @NotNull
  public List<? extends DeletionProcessor<T>> getDeletionProcessors() {
    return Collections.unmodifiableList( deletionProcessors );
  }

  public void addDeletionProcessor( @NotNull DeletionProcessor<T> processor ) {
    this.deletionProcessors.add( processor );
  }

  public void removeDeletionProcessor( @NotNull DeletionProcessor<T> processor ) {
    this.deletionProcessors.remove( processor );
  }

  /**
   * Notifes all registered processors that the given object will be deleted
   *
   * @param object the object that will be deleted
   */
  public void notifyWillBeDeleted( @NotNull T object ) {
    for ( DeletionProcessor<T> processor : deletionProcessors ) {
      processor.willBeDeleted( object );
    }
  }
}
