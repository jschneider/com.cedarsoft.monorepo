package de.xore;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 09.04.2004
 * Time: 00:58:08
 *
 *************************************************
 *  $$Log: XoreRuntimeException.java,v $
 *  $Revision 1.1  2004/05/11 14:40:24  johannes
 *  $init
 *  $
 *************************************************
 */

/**
 * <p/>
 * Date: 09.04.2004<br> Time: 00:58:08<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreRuntimeException extends RuntimeException {
  public XoreRuntimeException() {
  }

  public XoreRuntimeException( String message ) {
    super( message );
  }

  public XoreRuntimeException( Throwable cause ) {
    super( cause );
  }

  public XoreRuntimeException( String message, Throwable cause ) {
    super( message, cause );
  }

}
