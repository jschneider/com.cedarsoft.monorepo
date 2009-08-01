package de.xore.util.crypt;

import java.security.MessageDigest;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 05.05.2004
 * Time: 21:22:47
 *
 *************************************************
 *  $$Log: XoreCrypter.java,v $
 *  $Revision 1.1  2004/05/11 14:40:24  johannes
 *  $init
 *  $
 *************************************************
 */

/**
 * <p/>
 * Date: 05.05.2004<br> Time: 21:22:47<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreCrypter {
  private XoreCrypter() {
  }

  public static String encryp( String clearText ) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
      messageDigest.update( clearText.getBytes() );

      StringBuilder dig = new StringBuilder();
      byte[] digest = messageDigest.digest();
      for ( byte aDigest : digest ) {
        dig.append( Integer.toHexString( aDigest & 0xff ) );
      }
      return dig.toString();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }
}
