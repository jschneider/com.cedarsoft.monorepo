package com.cedarsoft.id;

/**
 * Helper class that offers support for id number generation
 * <p/>
 * Date: 09.10.2006<br>
 * Time: 13:44:33<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class NumberIdGenerator {
  private static int lastGivenId;

  public static synchronized int createId() {
    //todo check overflow
    return lastGivenId++;
  }
}
