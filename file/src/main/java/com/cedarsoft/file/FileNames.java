package com.cedarsoft.file;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a list of file names
 */
public class FileNames {
  @NotNull
  private final List<FileName> fileNames = new ArrayList<FileName>();

  public void add( @NotNull FileName fileName ) {
    if ( this.fileNames.contains( fileName ) ) {
      throw new IllegalArgumentException( "FileName still contained <" + fileName + ">" );
    }
    this.fileNames.add( fileName );
  }

  @NotNull
  public List<? extends FileName> getFileNames() {
    return Collections.unmodifiableList( fileNames );
  }
}
