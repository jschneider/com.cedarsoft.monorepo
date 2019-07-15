package com.cedarsoft.tests.pwa

import com.cedarsoft.open.kotlinpwa.common.Coords
import com.cedarsoft.open.kotlinpwa.common.PositionReport


/**
 * Represents the client model
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class ClientModel {
  /**
   * My coordinats
   */
  var myCoords: Coords? = null;

  var positionReport: PositionReport = PositionReport.empty

}
