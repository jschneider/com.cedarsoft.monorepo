package com.cedarsoft.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A abstract implementation of {@link TagManager} that holds the tags in memory.
 * This implementation can be used when the instance of the tag itself is not important.
 */
public abstract class MemoryTagManager<T> extends AbstractTagManager<T> {
  @NotNull
  protected final Map<T, WeakReference<Taggable>> store = new WeakHashMap<T, WeakReference<Taggable>>();

  protected MemoryTagManager() {
    super( new MemoryTagProvider() );
  }

  @NotNull
  @Override
  protected TagSet createTaggable( @NotNull T o ) {
    TagSet taggable = new TagSet( o );
    store.put( o, new WeakReference<Taggable>( taggable ) );
    return taggable;
  }

  @Override
  @NotNull
  public Taggable findTaggable( @NotNull T o ) throws NotFoundException {
    WeakReference<Taggable> weakReference = store.get( o );
    if ( weakReference == null ) {
      throw new NotFoundException();
    }
    Taggable taggable = weakReference.get();
    if ( taggable == null ) {
      throw new NotFoundException();
    }
    return taggable;
  }

  @NotNull
  protected T getObject( @NotNull Taggable taggable ) {
    for ( Map.Entry<T, WeakReference<Taggable>> entry : store.entrySet() ) {
      //noinspection ObjectEquality
      if ( entry.getValue().get() == taggable ) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException( "No object found for " + taggable );
  }
}
