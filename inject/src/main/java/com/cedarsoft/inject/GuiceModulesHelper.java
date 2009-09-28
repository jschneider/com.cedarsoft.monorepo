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
  public static Result minimize( @NotNull List<? extends Module> modules, @NotNull Class<?> testType ) {
    //Verify to ensure it works with all modules
    verifyInjection( modules, testType );

    return minimize( new Result( modules ), testType );
  }

  @NotNull
  public static Result minimize( @NotNull Result result, @NotNull Class<?> testType ) {
    //Iterate over all types (copy because the result is updated)
    List<Module> modules = new ArrayList<Module>( result.getTypes() );
    for ( Module current : modules ) {

      try {
        List<Module> copy = new ArrayList<Module>( modules );
        copy.remove( current );
        verifyInjection( copy, testType );

        //Update the result
        result.remove( current );

        if ( copy.isEmpty() ) {
          result.removeAll();
          return result; //fast exit
        }

        //Try to minimize further
        return minimize( result, testType );
      } catch ( Exception ignore ) {
      }
    }

    return result; //no minimization
  }

  private static void verifyInjection( @NotNull Iterable<? extends Module> modules, @NotNull Class<?> testType ) {
    Guice.createInjector( modules ).getInstance( testType );
  }

  public static void assertMinimizeNotPossible( @NotNull List<? extends Module> modules, @NotNull Class<?> testType ) throws AssertionError {
    GuiceModulesHelper.Result minimal = minimize( modules, testType );
    if ( !minimal.getRemoved().isEmpty() ) {
      throw new AssertionError( "Can be minimized:\nRemove:\n" + minimal.getRemovedClassNamesAsString() + minimal.asInstantiations() );
    }
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
    public String getRemovedClassNamesAsString() {
      StringBuilder builder = new StringBuilder();

      for ( Module module : removed ) {
        builder.append( "- " );
        builder.append( module.getClass().getName() );
        builder.append( "\n" );
      }

      return builder.toString();
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

