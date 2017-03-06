package com.cedarsoft.swing.common.dialog;

import org.assertj.core.api.Assertions;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DialogIconUtilsTest {
  @Test
  public void basics() throws Exception {
    Assertions.assertThat(AbstractDialog.Icons.ERROR.getImage()).isNotNull();
    Assertions.assertThat(AbstractDialog.Icons.INFORMATION.getImage()).isNotNull();
    Assertions.assertThat(AbstractDialog.Icons.QUESTION.getImage()).isNotNull();
    Assertions.assertThat(AbstractDialog.Icons.WARNING.getImage()).isNotNull();
  }
}
