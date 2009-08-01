package de.xore.util.persistence;

/**
 * Is thrown, if an exception occured while accessing the database.
 * <p/>
 * Date: 21.06.2006<br>
 * Time: 23:07:32<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class DatabaseException extends RuntimeException {
  public DatabaseException() {
  }

  public DatabaseException( String message ) {
    super( message );
  }

  public DatabaseException( String message, Throwable cause ) {
    super( message, cause );
  }

  public DatabaseException( Throwable cause ) {
    super( cause );
  }
}