package com.cedarsoft.test.utils

import org.junit.jupiter.api.extension.ExtendWith

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@ExtendWith(JodaDateTimeZoneExtension::class)
annotation class WithJodaDateTimeZone(val value: String)
