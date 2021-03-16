package com.cedarsoft.test.utils

import com.cedarsoft.unit.si.ms
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Sets the random generator with a seeded value
 *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@ExtendWith(WithSeededRandomProviderExtension::class)
annotation class RandomWithSeed(val seed: @ms Int = 42)
