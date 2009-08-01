package com.cedarsoft.utils.io;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Resolves the relative path
 */
public class RelativePathFinder {
  private static final char BACKSLASH = '\\';
  private static final char SLASH = '/';

  /**
   * Calculates the relative path
   *
   * @param target    the target path
   * @param base      the base (a directory)
   * @param separator the separator
   * @return the relative path pointing to the target (from the base)
   */
  public static String getRelativePath( @NotNull @NonNls final String target, @NotNull @NonNls final String base, @NotNull @NonNls final String separator ) {
    //
    // remove trailing file separator
    //
    @NonNls
    String canonicalBase = base;
    if ( base.charAt( base.length() - 1 ) == SLASH || base.charAt( base.length() - 1 ) == BACKSLASH ) {
      canonicalBase = base.substring( 0, base.length() - 1 );
    }

    //
    // get canonical name of target and remove trailing separator
    //
    @NonNls
    String canonicalTarget = target;

    if ( canonicalTarget.charAt( canonicalTarget.length() - 1 ) == SLASH || canonicalTarget.charAt( canonicalTarget.length() - 1 ) == BACKSLASH ) {
      canonicalTarget = canonicalTarget.substring( 0, canonicalTarget.length() - 1 );
    }

    if ( canonicalTarget.equals( canonicalBase ) ) {
      return ".";
    }

    //
    // see if the prefixes are the same
    //
    if ( canonicalBase.substring( 0, 2 ).equals( "\\\\" ) ) {
      //
      // UNC file name, if target file doesn't also start with same
      // server name, don't go there
      int endPrefix = canonicalBase.indexOf( BACKSLASH, 2 );
      @NonNls
      String prefix1 = canonicalBase.substring( 0, endPrefix );
      @NonNls
      String prefix2 = canonicalTarget.substring( 0, endPrefix );
      if ( !prefix1.equals( prefix2 ) ) {
        return canonicalTarget;
      }
    } else {
      if ( canonicalBase.substring( 1, 3 ).equals( ":\\" ) ) {
        int endPrefix = 2;
        @NonNls
        String prefix1 = canonicalBase.substring( 0, endPrefix );
        @NonNls
        String prefix2 = canonicalTarget.substring( 0, endPrefix );
        if ( !prefix1.equals( prefix2 ) ) {
          return canonicalTarget;
        }
      } else {
        if ( canonicalBase.charAt( 0 ) == SLASH ) {
          if ( canonicalTarget.charAt( 0 ) != SLASH ) {
            return canonicalTarget;
          }
        }
      }
    }

    // char separator = File.separatorChar;
    int minLength = canonicalBase.length();

    if ( canonicalTarget.length() < minLength ) {
      minLength = canonicalTarget.length();
    }

    int firstDifference = minLength + 1;

    //
    // walk to the shorter of the two paths
    // finding the last separator they have in common
    int lastSeparator = -1;
    for ( int i = 0; i < minLength; i++ ) {
      if ( canonicalTarget.charAt( i ) == canonicalBase.charAt( i ) ) {
        if ( canonicalTarget.charAt( i ) == SLASH
          || canonicalTarget.charAt( i ) == BACKSLASH ) {
          lastSeparator = i;
        }
      } else {
        firstDifference = lastSeparator + 1;
        break;
      }
    }

    StringBuilder relativePath = new StringBuilder( 50 );

    //
    // walk from the first difference to the end of the base
    // adding "../" for each separator encountered
    //
    if ( canonicalBase.length() > firstDifference ) {
      relativePath.append( ".." );
      for ( int i = firstDifference; i < canonicalBase.length(); i++ ) {
        if ( canonicalBase.charAt( i ) == SLASH
          || canonicalBase.charAt( i ) == BACKSLASH ) {
          relativePath.append( separator );
          relativePath.append( ".." );
        }
      }
    }

    if ( canonicalTarget.length() > firstDifference ) {
      //
      // append the rest of the target
      //

      if ( relativePath.length() > 0 ) {
        relativePath.append( separator );
      }
      relativePath.append( canonicalTarget.substring( firstDifference ) );
    }

    return relativePath.toString();
  }

  @NotNull
  @NonNls
  public static File getRelativePath( @NotNull @NonNls File target, @NotNull @NonNls File base, @NotNull @NonNls String pathSeparator ) {
    return new File( getRelativePath( target.getPath(), base.getPath(), pathSeparator ) );
  }

  public static File getRelativePath( @NotNull @NonNls File target, @NotNull @NonNls File base ) {
    return getRelativePath( target, base, File.separator );
  }

  @NotNull
  @NonNls
  public static String getRelativePath( @NotNull @NonNls String targetPath, @NotNull @NonNls String basePath ) {
    return getRelativePath( targetPath, basePath, File.separator );
  }
}
