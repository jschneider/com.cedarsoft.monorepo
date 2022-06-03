package com.cedarsoft.javafx.test

import com.cedarsoft.test.utils.DisableIfHeadless
import javafx.application.Platform
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension

/**
 * Marks a test as a JavaFX test that is run using [ApplicationExtension]
 */
@DisableIfHeadless
@ExtendWith(ApplicationExtension::class)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class JavaFxTest


/**
 * Throws an exception if the current thread is not the Java FX UI thread
 */
fun assertFxThread() {
  Assertions.assertThat(Platform.isFxApplicationThread()).describedAs("Current Thread: <${Thread.currentThread().name}>").isTrue()
}

/**
 * Throws an exception if the current thread is the Java FX UI thread
 */
fun assertNotFxThread() {
  Assertions.assertThat(Platform.isFxApplicationThread()).describedAs("Current Thread: <${Thread.currentThread().name}>").isFalse()
}
