package com.cedarsoft.commons.concurrent

import com.cedarsoft.exceptions.ApplicationException

/**
 * Exception for heartbeat
 */
class HeartbeatException(details: Details, parameters: Map<String, Any>?) : ApplicationException(details, parameters) {

}
