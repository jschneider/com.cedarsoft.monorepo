package com.cedarsoft.photos.tools;

/**
 * Is thrown if the the command line tool is not available
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CmdLineToolNotAvailableException extends Exception {
  public CmdLineToolNotAvailableException(String message) {
    super(message);
  }
}
