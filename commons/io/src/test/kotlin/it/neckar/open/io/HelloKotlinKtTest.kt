package it.neckar.open.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 */
class HelloKotlinKtTest {
    @Test
    fun name() {
        assertThat("asdf").hasSize(4)
    }
}
