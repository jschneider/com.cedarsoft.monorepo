package com.cedarsoft.photos;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LinkByDateCreatorTest {
  @Test
  public void suffx() throws Exception {
    assertThat(LinkByDateCreator.addSuffix("asdf.txt", 0)).isEqualTo("asdf.txt");
    assertThat(LinkByDateCreator.addSuffix("asdf.txt", 1)).isEqualTo("asdf_1.txt");
    assertThat(LinkByDateCreator.addSuffix("asdf.txt", 37)).isEqualTo("asdf_37.txt");

    assertThat(LinkByDateCreator.addSuffix("asdf_txt", 37)).isEqualTo("asdf_txt_37");
  }
}