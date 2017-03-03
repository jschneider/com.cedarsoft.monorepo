package com.cedarsoft.swing.common;

import org.junit.*;

import javax.imageio.ImageIO;
import java.awt.Image;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NotSoVolatileImageTest {
  @Test
  public void basic() throws Exception {
    Image image = ImageIO.read(getClass().getResourceAsStream("elephant.jpg"));
    NotSoVolatileImage notSoVolatileImage = new NotSoVolatileImage(image);

    assertThat(notSoVolatileImage.getVolatileImage()).isNotNull();
    notSoVolatileImage.flush();
  }
}