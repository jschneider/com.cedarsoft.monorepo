package com.cedarsoft.inject;

import com.google.inject.Guice;
import com.google.inject.Module;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class GuiceModulesHelper {
  private GuiceModulesHelper() {
  }

  @NotNull
  public static Result minimize( @NotNull List<? extends Module> moduleTypes, @NotNull Class<?> testType ) {
    Result best = new Result( moduleTypes );

    //Iterate over all types
    for ( Module current : moduleTypes ) {
      try {
        List<Module> copy = new ArrayList<Module>( moduleTypes );
        copy.remove( current );
        Guice.createInjector( copy ).getInstance( testType );

        best.remove( current );


        if ( copy.isEmpty() ) {
          best.removeAll();
          return best; //fast exit
        }

        //Check further
        Result currentBest = minimize( copy, testType );

        if ( best.size() > currentBest.size() ) {
          best = currentBest;
        }
      } catch ( Exception ignore ) {
      }
    }

    return best;
  }

  public static class Result {
    @NotNull
    private final List<? extends Module> types;

    @NotNull
    private final List<Module> removed = new ArrayList<Module>();

    public Result( @NotNull List<? extends Module> types ) {
      this.types = new ArrayList<Module>( types );
    }

    public int size() {
      return types.size();
    }

    public void removeAll() {
      removed.addAll( types );
      types.clear();
    }

    public void remove( @NotNull Module toRemove ) {
      types.remove( toRemove );
      removed.add( toRemove );
    }

    @NotNull
    public List<? extends Module> getTypes() {
      return Collections.unmodifiableList( types );
    }

    @NotNull
    public List<? extends Module> getRemoved() {
      return Collections.unmodifiableList( removed );
    }

    @NotNull
    @NonNls
    public String asInstantiations() {
      StringBuilder builder = new StringBuilder();

      for ( Iterator<? extends Module> iterator = types.iterator(); iterator.hasNext(); ) {
        Module module = iterator.next();
        builder.append( "new " ).append( module.getClass().getName() ).append( "()" );

        if ( iterator.hasNext() ) {
          builder.append( ", " );
        }
      }

      return builder.toString();
    }
  }
}

