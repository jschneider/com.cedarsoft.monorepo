package com.cedarsoft.osgi.validator;

import com.google.common.base.Joiner;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ValidationFailedException extends Exception{
  @Nonnull
  private final String relativePath;
  private final int failingIndex;

  public ValidationFailedException( @Nonnull String relativePath, @Nonnull List<? extends String> splitPath, int failingIndex, @Nonnull String message ) {
    super( createMessage( relativePath, splitPath, failingIndex, message ) );
    this.relativePath = relativePath;
    this.failingIndex = failingIndex;
  }

  @Nonnull
  private static String createMessage( @Nonnull String relativePath, @Nonnull List<? extends String> splitPath, int failingIndex, @Nonnull String message ) {
    StringBuilder builder = new StringBuilder();
    builder.append( "Invalid path <" ).append( relativePath ).append( ">" );

    if ( failingIndex > 0 ) {
      String failingPart = Joiner.on( File.separator ).join( splitPath.subList( 0, failingIndex + 1 ) );
      builder.append( ". Failed at <" ).append( failingPart ).append( ">" );
    }

    builder.append( ": " );
    builder.append( message );

    return builder.toString();
  }

  public int getFailingIndex() {
    return failingIndex;
  }

  @Nonnull
  public String getRelativePath() {
    return relativePath;
  }
}
