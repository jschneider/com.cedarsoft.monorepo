package com.cedarsoft.test.utils

import com.cedarsoft.unit.si.ms
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Sets the time returned by nowMillis to the given fixed value.
 *
 * If there is a parameter `nowProvider: FixedNowProvider` added to the test methods, the current instance of [com.cedarsoft.common.time.FixedNowProvider] is assigned.
 *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@ExtendWith(FixedNowProviderExtension::class)
annotation class FixedTime(val value: @ms Double = 0.0)
