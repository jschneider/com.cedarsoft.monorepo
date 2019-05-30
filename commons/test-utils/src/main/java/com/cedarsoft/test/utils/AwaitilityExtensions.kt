package com.cedarsoft.test.utils

import org.awaitility.core.ConditionFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
fun ConditionFactory.untilAtomicIsTrue(atomicBoolean: AtomicBoolean) {
  return untilAtomic(atomicBoolean, org.hamcrest.CoreMatchers.`is`(true))
}

fun ConditionFactory.atMostMillis(millis: Long): ConditionFactory {
  return atMost(millis, TimeUnit.MILLISECONDS)
}
