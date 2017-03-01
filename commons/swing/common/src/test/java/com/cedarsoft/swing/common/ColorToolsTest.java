package com.cedarsoft.swing.common;

import org.junit.*;

import java.awt.Color;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ColorToolsTest {
  @Test
  public void parseRgb() throws Exception {
    assertThat(ColorTools.parseRgbColor("1,2,3")).isEqualTo(new Color(1, 2, 3));
    assertThat(ColorTools.parseRgbColor("0,254,255")).isEqualTo(new Color(0, 254, 255));
    assertThat(ColorTools.parseRgbColor("1, 2 , 255 ")).isEqualTo(new Color(1, 2, 255));
  }

  @Test(expected = IllegalArgumentException.class)
  public void bounds() throws Exception {
    ColorTools.parseRgbColor("1, 2 , 256 ");
  }

  @Test
  public void rgb2string() throws Exception {
    assertThat(ColorTools.toRgbString(new Color(12, 14, 222))).isEqualTo("12,14,222");
  }
}