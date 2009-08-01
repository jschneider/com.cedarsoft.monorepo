package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ElementsChangedEvent<T> {
  @NotNull
  private final ObservableCollection<T> source;

  /**
   * Contains the changed elements
   */
  @NotNull
  private final List<? extends T> elements;

  /**
   * Contains the indicies
   */
  @NotNull
  private final List<? extends Integer> indicies;

  public ElementsChangedEvent( @NotNull ObservableCollection<T> source, @NotNull List<? extends T> elements, int lowestIndex, int highestIndex ) {
    this( source, elements, createIndicies( lowestIndex, highestIndex ) );
  }

  public ElementsChangedEvent( @NotNull ObservableCollection<T> source, @NotNull List<? extends T> elements, @NotNull List<? extends Integer> indicies ) {
    this.source = source;

    if ( elements.size() != indicies.size() ) {
      throw new IllegalArgumentException( "Invalid arguments. Indicies must be of same size as elements" );
    }

    //noinspection AssignmentToCollectionOrArrayFieldFromParameter
    this.elements = elements;
    //noinspection AssignmentToCollectionOrArrayFieldFromParameter
    this.indicies = indicies;
  }

  @NotNull
  public ObservableCollection<T> getSource() {
    return source;
  }

  @NotNull
  public List<? extends T> getElements() {
    //noinspection ReturnOfCollectionOrArrayField
    return elements;
  }

  @NotNull
  public List<? extends Integer> getIndicies() {
    //noinspection ReturnOfCollectionOrArrayField
    return indicies;
  }

  /**
   * Creates a new list with indicies from the lower to the upper (inclusive)
   *
   * @param lower the lower (inclusive)
   * @param upper the upper border (inclusive)
   * @return a list of indicies
   */
  @NotNull
  public static List<Integer> createIndicies( int lower, int upper ) {
    List<Integer> indicies = new ArrayList<Integer>();

    for ( int i = lower; i <= upper; i++ ) {
      indicies.add( i );
    }

    return indicies;
  }
}