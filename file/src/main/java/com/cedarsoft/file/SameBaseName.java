package com.cedarsoft.file;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 */
public class SameBaseName {
  @NotNull
  private final BaseName baseName;

  @NotNull
  private final Collection<FileName> fileNames = new HashSet<FileName>();

  public SameBaseName( @NotNull BaseName baseName ) {
    this.baseName = baseName;
  }

  @NotNull
  public BaseName getBaseName() {
    return baseName;
  }

  public void add( @NotNull FileName fileName ) {
    if ( !fileNames.add( fileName ) ) {
      throw new IllegalArgumentException( "FileName still contained " + fileName );
    }
  }

  @NotNull
  public Collection<? extends FileName> getFileNames() {
    return Collections.unmodifiableCollection( fileNames );
  }
}
