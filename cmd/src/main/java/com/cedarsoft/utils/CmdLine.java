package com.cedarsoft.utils;

import com.cedarsoft.renderer.Renderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Provides access to the command line
 */
public interface CmdLine {
  /**
   * Reads a boolean from the command line
   *
   * @param message the message that is shown
   * @return the value the user has entered
   *
   * @throws IOException
   */
  boolean readBoolean( @NotNull String message ) throws IOException;

  /**
   * Prints an error on the console
   *
   * @param message the error message
   * @param objects the objects
   */
  void error( @NotNull String message, @NotNull Object... objects );

  /**
   * Prints a warning
   *
   * @param message the message
   * @param objects the objects
   */
  void warning( @NotNull String message, @NotNull Object... objects );

  /**
   * Prints a success message
   *
   * @param message the message
   * @param objects the objects
   */
  void success( @NotNull String message, @NotNull Object... objects );

  /**
   * Reads a string form the console
   *
   * @param message the mssage
   * @return the string
   */
  @NotNull
  String read( @NotNull String message );

  /**
   * Reads a string from the console. The user may optinally select the given default value
   *
   * @param message      the message
   * @param defaultValue the default value
   * @return the string
   */
  @NotNull
  String read( @NotNull String message, @Nullable String defaultValue );

  /**
   * Reads a string
   *
   * @param message  the message
   * @param elements the elements that may be selected
   * @return the entered value (a free value or one of the elements)
   */
  @NotNull
  String read( @NotNull String message, @NotNull List<String> elements );

  /**
   * Reads an int from the console
   *
   * @param message the message
   * @param lower   the lower bounds
   * @param upper   the upper bounds
   * @return the read int
   */
  int readInt( @NotNull String message, int lower, int upper );

  /**
   * Reads an int from the console
   *
   * @param message the message
   * @return the int
   *
   * @throws IOException
   */
  int readInt( @NotNull String message ) throws IOException;

  /**
   * Select an element from the list
   *
   * @param message   the message
   * @param elements  the elements that may be choosen @return the selected element
   * @param presenter an optional presenter that creates a string representation for the elements
   * @return the selected element
   */
  @NotNull
  <T> T readSelection( @NotNull String message, @NotNull List<? extends T> elements, @Nullable Renderer<? super T, Object> presenter );

  /**
   * Selects an element
   *
   * @param message  the message
   * @param elements the elements
   * @return the selected element
   */
  @NotNull
  <T> T readSelection( @NotNull String message, @NotNull List<? extends T> elements );

  /**
   * Pauses the script
   *
   * @param seconds the seconds that it is paused
   */
  void pause( int seconds );

  /**
   * Prints out a new line
   */
  void outNl();

  /**
   * Prints a message
   *
   * @param message the message
   * @param objects the objects
   */
  void out( @NotNull String message, @NotNull Object... objects );

  /**
   * Redirect the output of the given process
   *
   * @param process the process the output for is redirected
   */
  void out( @NotNull Process process );

  /**
   * Reads a string from the console
   *
   * @param message   the message
   * @param elements  the elements (the user may select one of them)
   * @param presenter the presenter
   * @return the string that has been entered manually or the object that has been selected (String or T)
   */
  @NotNull
  <T> T read( @NotNull String message, @NotNull List<? extends T> elements, @Nullable Renderer<T, Object> presenter, @NotNull ObjectFactory<T> objectFactory );

  /**
   * Reads a string from the console
   *
   * @param message     the message
   * @param elements    the elements
   * @param preselected the preselected string
   * @return the read string from the console (one of the elements, the preselected value or a newly entered value)
   */
  @NotNull
  String read( @NotNull String message, @NotNull List<String> elements, @NotNull String preselected );
}