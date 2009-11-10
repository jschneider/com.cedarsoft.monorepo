package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 */
public class BaseNameAwareFileNames {
  @NotNull
  private final SortedMap<BaseName, FileNames> entries = new TreeMap<BaseName, FileNames>();

  public void add( @NotNull FileName fileName ) {
    getEntry( fileName.getBaseName() ).add( fileName );
  }

  @NotNull
  public Collection<? extends Map.Entry<BaseName, FileNames>> getEntries() {
    return Collections.unmodifiableCollection( entries.entrySet() );
  }

  @NotNull
  public FileNames getEntry( @NotNull @NonNls BaseName baseName ) {
    FileNames found = entries.get( baseName );
    if ( found != null ) {
      return found;
    }

    FileNames created = new FileNames();
    entries.put( baseName, created );
    return created;
  }

  public int size() {
    return entries.size();
  }
}
