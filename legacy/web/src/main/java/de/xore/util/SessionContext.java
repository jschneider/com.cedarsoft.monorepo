package de.xore.util;

import java.io.Serializable;

/*
* Created by XoreSystems (Johannes Schneider).
* User: Johannes
* Date: 08.04.2004
* Time: 17:54:24
*/

/**
 * <p/>
 * Date: 08.04.2004<br> Time: 17:54:24<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public interface SessionContext extends Serializable {
  String getRoot();

}
