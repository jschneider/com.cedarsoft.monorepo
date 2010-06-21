package com.cedarsoft.codegen.model.test;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Room {
  @NotNull
  @NonNls
  private final String description;

  @NotNull
  private final List<Window> windows = new ArrayList<Window>();

  @NotNull
  private final List<Door> doors = new ArrayList<Door>();

  public Room( @NotNull String description ) {
    this.description = description;
  }

  public Room( @NotNull String description, Collection<? extends Window> windows, Collection<? extends Door> doors ) {
    this.description = description;
    this.windows.addAll( windows );
    this.doors.addAll( doors );
  }

  public void addDoor( @NotNull Door door ) {
    this.doors.add( door );
  }

  public void addWindow( @NotNull Window window ) {
    this.windows.add( window );
  }

  @NotNull
  public String getDescription() {
    return description;
  }

  @NotNull
  public List<? extends Window> getWindows() {
    return Collections.unmodifiableList( windows );
  }

  @NotNull
  public List<? extends Door> getDoors() {
    return Collections.unmodifiableList( doors );
  }

  public void setDoors( @NotNull List<? extends Door> doors ) {
    this.doors.clear();
    this.doors.addAll( doors );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Room ) ) return false;

    Room room = ( Room ) o;

    if ( !description.equals( room.description ) ) return false;
    if ( !doors.equals( room.doors ) ) return false;
    if ( !windows.equals( room.windows ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = description.hashCode();
    result = 31 * result + windows.hashCode();
    result = 31 * result + doors.hashCode();
    return result;
  }
}
