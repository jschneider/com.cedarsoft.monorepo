package com.cedarsoft.test.utils

import org.junit.jupiter.api.extension.ExtendWith

/**
 * Use {@link WithTempFiles} at the class/method and add {@link TempFolder} oder {@link TempFile} to the test method parameters
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@ExtendWith(TemporaryFolderExtension::class)
annotation class WithTempFiles
