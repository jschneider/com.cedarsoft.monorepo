package com.cedarsoft.photos;

import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;
import com.cedarsoft.crypt.HashCalculator;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SplitHashTest {
  @Test
  public void basic() throws Exception {
    Hash hash = HashCalculator.calculate(ImageStorage.ALGORITHM, "thecontent");
    SplitHash splitHash = SplitHash.split(hash);

    assertThat(hash.getValueAsHex()).isEqualTo("8ba871f31f3c8ad7d74591859e60f42fe89852ceb407fcd13f32433d37b751db");
    assertThat(splitHash.getFirstPart()).isEqualTo("8b");
    assertThat(splitHash.getLeftover()).isEqualTo("a871f31f3c8ad7d74591859e60f42fe89852ceb407fcd13f32433d37b751db");
  }
}


