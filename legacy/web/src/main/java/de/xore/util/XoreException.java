package de.xore.util;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 08.04.2004
 * Time: 17:59:38
 *
 *************************************************
 *  $$Log: XoreException.java,v $
 *  $Revision 1.1  2004/05/11 14:40:24  johannes
 *  $init
 *  $
 *************************************************
 */

/**
 * <p/>
 * Date: 08.04.2004<br> Time: 17:59:38<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreException extends Exception {
  public XoreException() {
  }

  public XoreException( String message ) {
    super( message );
  }

  public XoreException( Throwable cause ) {
    super( cause );
  }

  public XoreException( String message, Throwable cause ) {
    super( message, cause );
  }
}
