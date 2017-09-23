package com.cedarsoft.test.utils

/**
 *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class TempFile(val value: String = "")