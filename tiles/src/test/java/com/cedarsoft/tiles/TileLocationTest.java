package com.cedarsoft.tiles;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TileLocationTest {
  @Test
  public void testIt() throws Exception {
    TileLocation identifier = new TileLocation(0, 0, 1, 1, 100, 100);

    assertThat(identifier.getX()).isEqualTo(0);
    assertThat(identifier.getY()).isEqualTo(0);

    assertThat(identifier.getMinX()).isEqualTo(0);
    assertThat(identifier.getMinY()).isEqualTo(0);
    assertThat(identifier.getMaxX()).isEqualTo(100);
    assertThat(identifier.getMaxY()).isEqualTo(100);

    assertThat(identifier.getModelMinX()).isEqualTo(0);
    assertThat(identifier.getModelMinY()).isEqualTo(0);
    assertThat(identifier.getModelMaxX()).isEqualTo(100);
    assertThat(identifier.getModelMaxY()).isEqualTo(100);

    assertThat(identifier.contains(0, 100)).isTrue();
    assertThat(identifier.contains(99, 101)).isTrue();
    assertThat(identifier.contains(100, 101)).isTrue();

    assertThat(identifier.contains(-1, 1)).isTrue();
    assertThat(identifier.contains(-1, 0)).isTrue();

    assertThat(identifier.contains(-1, 101)).isTrue();

    assertThat(identifier.contains(-2, -1)).isFalse();
    assertThat(identifier.contains(101, 102)).isFalse();
  }

  @Test
  public void testIt2() throws Exception {
    TileLocation identifier = new TileLocation(1, 1, 2, 2, 100, 100);

    assertThat(identifier.getX()).isEqualTo(1);
    assertThat(identifier.getY()).isEqualTo(1);

    assertThat(identifier.getMinX()).isEqualTo(100);
    assertThat(identifier.getMinY()).isEqualTo(100);
    assertThat(identifier.getMaxX()).isEqualTo(200);
    assertThat(identifier.getMaxY()).isEqualTo(200);

    assertThat(identifier.getModelMinX()).isEqualTo(50);
    assertThat(identifier.getModelMinY()).isEqualTo(50);
    assertThat(identifier.getModelMaxX()).isEqualTo(100);
    assertThat(identifier.getModelMaxY()).isEqualTo(100);

    assertThat(identifier.contains(50, 100)).isTrue();
    assertThat(identifier.contains(99, 101)).isTrue();
    assertThat(identifier.contains(100, 101)).isTrue();

    assertThat(identifier.contains(49, 51)).isTrue();
    assertThat(identifier.contains(-1, 50)).isTrue();

    assertThat(identifier.contains(49, 101)).isTrue();

    assertThat(identifier.contains(-2, 49)).isFalse();
    assertThat(identifier.contains(101, 102)).isFalse();
  }
}