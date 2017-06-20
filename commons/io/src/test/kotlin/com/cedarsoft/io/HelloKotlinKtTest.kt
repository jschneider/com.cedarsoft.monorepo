package com.cedarsoft.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class HelloKotlinKtTest {
    @Test
    fun name() {
        assertThat("asdf").hasSize(4)
    }
}
