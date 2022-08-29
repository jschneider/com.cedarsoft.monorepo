package com.cedarsoft.test.utils

import com.cedarsoft.test.utils.DisableIfNotLinuxCondition
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Run tests only on Linux
 *
 */
@ExtendWith(DisableIfNotLinuxCondition::class)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class OnlyLinux
