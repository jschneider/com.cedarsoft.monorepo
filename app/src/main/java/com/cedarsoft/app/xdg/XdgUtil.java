package com.cedarsoft.app.xdg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Created from http://standards.freedesktop.org/basedir-spec/basedir-spec-latest.html
 */
public class XdgUtil {
  @Nonnull
  private static final File HOME_DIR = getHomeDir();

  @Nonnull
  private static File getHomeDir() {
    String userHomeAsString = System.getProperty( "user.home" );
    if ( userHomeAsString == null ) {
      throw new IllegalStateException( "No property found for user.home" );
    }

    File home = new File( userHomeAsString );
    if ( !home.isDirectory() ) {
      throw new IllegalStateException( "User home <" + home.getAbsolutePath() + "> is not a directory" );
    }

    return home;
  }

  /**
   * The base directory relative to which user specific data files should be stored.
   * If $XDG_DATA_HOME is either not set or empty, a default equal to $HOME/.local/share should be used.
   */
  @Nonnull
  public static final String XDG_DATA_HOME = "XDG_DATA_HOME";
  @Nonnull
  public static final File XDG_DATA_HOME_DEFAULT = new File( HOME_DIR, ".local/share" );

  /**
   * The base directory relative to which user specific configuration files should be stored.
   * If $XDG_CONFIG_HOME is either not set or empty, a default equal to $HOME/.config should be used.
   */
  public static final String XDG_CONFIG_HOME = "XDG_CONFIG_HOME";
  @Nonnull
  public static final File XDG_CONFIG_HOME_DEFAULT = new File( HOME_DIR, ".config" );

  /**
   * <p>
   * The preference-ordered set of base directories to search for data files in addition to
   * the $XDG_DATA_HOME base directory.
   * The directories in $XDG_DATA_DIRS should be separated with a colon ':'.
   * </p>
   * <p/>
   * <p>
   * If $XDG_DATA_DIRS is either not set or empty, a value equal to /usr/local/share/:/usr/share/ should be used.
   * </p>
   */
  public static final String XDG_DATA_DIRS = "XDG_DATA_DIRS";


  /**
   * <p>
   * The preference-ordered set of base directories to search for configuration
   * files in addition to the $XDG_CONFIG_HOME base directory.
   * The directories in $XDG_CONFIG_DIRS should be seperated with a colon ':'.
   * </p>
   * <p/>
   * <p>
   * If $XDG_CONFIG_DIRS is either not set or empty, a value equal to /etc/xdg should be used.
   * </p>
   * <p>
   * The order of base directories denotes their importance; the first directory listed is the most important.
   * When the same information is defined in multiple places the information defined relative to the more important base directory takes precedent.
   * The base directory defined by $XDG_DATA_HOME is considered more important than any of the base directories defined by $XDG_DATA_DIRS.
   * The base directory defined by $XDG_CONFIG_HOME is considered more important than any of the base directories defined by $XDG_CONFIG_DIRS.
   * </p>
   */
  public static final String XDG_CONFIG_DIRS = "XDG_CONFIG_DIRS";
  /**
   * The base directory relative to which user specific non-essential data files should be stored.
   * If $XDG_CACHE_HOME is either not set or empty, a default equal to $HOME/.cache should be used.
   */
  public static final String XDG_CACHE_HOME = "XDG_CACHE_HOME";
  @Nonnull
  public static final File XDG_CACHE_HOME_DEFAULT = new File( HOME_DIR, ".cache" );

  /**
   * The base directory relative to which user-specific non-essential runtime files and other file objects (such as sockets, named pipes, ...) should be stored.
   * The directory MUST be owned by the user, and he MUST be the only one having read and write access to it. Its Unix access mode MUST be 0700.
   */
  public static final String XDG_RUNTIME_DIR = "XDG_RUNTIME_DIR";


  /**
   * Returns the data home dir
   */
  @Nonnull
  public static File getDataHome() {
    String value = System.getenv().get( XDG_DATA_HOME );
    return getDir( value, XDG_DATA_HOME_DEFAULT );
  }

  /**
   * Returns the config home dir
   */
  @Nonnull
  public static File getConfigHome() {
    String value = System.getenv().get( XDG_CONFIG_HOME );
    return getDir( value, XDG_CONFIG_HOME_DEFAULT );
  }

  /**
   * Returns the cache home dir
   */
  @Nonnull
  public static File getCacheHome() {
    String value = System.getenv().get( XDG_CACHE_HOME );
    return getDir( value, XDG_CACHE_HOME_DEFAULT );
  }

  @Nonnull
  private static File getDir( @Nullable String dirName, @Nonnull File defaultDir ) {
    if ( dirName == null ) {
      return defaultDir;
    }
    return new File( dirName );
  }

}