package it.neckar.open.concurrent

import it.neckar.open.exceptions.ApplicationException

/**
 * Exception for heartbeat
 */
class HeartbeatException(details: Details, parameters: Map<String, Any>?) : ApplicationException(details, parameters) {

}
