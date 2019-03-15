package com.cedarsoft.javafx.test

import com.cedarsoft.test.utils.DisableIfHeadless
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension

/**
 * Marks a test as a JavaFX test that is run using [ApplicationExtension]
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@DisableIfHeadless
@ExtendWith(ApplicationExtension::class)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class JavaFxTest
