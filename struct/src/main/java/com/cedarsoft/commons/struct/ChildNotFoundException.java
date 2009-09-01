package com.cedarsoft.commons.struct;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NotNull;

/**
 * <p/>
 * Date: 12.10.2006<br>
 * Time: 21:05:19<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class ChildNotFoundException extends NotFoundException {
  private final Path path;

  public ChildNotFoundException( @NotNull Path path ) {
    super( "Child not found <" + path.toString() + '>' );
    this.path = path;
  }

  @NotNull
  public Path getPath() {
    return path;
  }
}
