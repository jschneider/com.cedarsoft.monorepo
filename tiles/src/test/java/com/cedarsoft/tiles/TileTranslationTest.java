package com.cedarsoft.tiles;

import org.junit.*;

import javax.annotation.Nonnull;
import java.awt.geom.Line2D;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TileTranslationTest {
  @Test
  public void testContinousX() throws Exception {
    for (int x = 0; x < 10; x++) {
      TileTranslation translation = new TileTranslation(x, 0, 1, 1, 200, 200);
      TileTranslation translationNext = new TileTranslation(x + 1, 0, 1, 1, 200, 200);

      assertThat(translation.getMaxXModel()).isEqualTo(translationNext.getMinXModel());
    }
  }

  @Test
  public void testContinousY() throws Exception {
    for (int y = 0; y < 10; y++) {
      TileTranslation translation = new TileTranslation(0, y, 1, 1, 200, 200);
      TileTranslation translationNext = new TileTranslation(0, y + 1, 1, 1, 200, 200);

      assertThat(translation.getMaxYModel()).isEqualTo(translationNext.getMinYModel());
    }
  }

  @Test
  public void testContinousBoth() throws Exception {
    for (int x = -10; x < 10; x++) {
      for (int y = -10; y < 10; y++) {
        TileTranslation translation = new TileTranslation(x, y, 1, 1, 200, 200);
        checkPlausibility(translation);

        TileTranslation translationNextX = new TileTranslation(x + 1, y, 1, 1, 200, 200);
        TileTranslation translationNextY = new TileTranslation(x, y + 1, 1, 1, 200, 200);

        assertThat(translation.getMaxXModel()).isEqualTo(translationNextX.getMinXModel());
        assertThat(translation.getMaxYModel()).isEqualTo(translationNextY.getMinYModel());

        assertThat(translation.getMaxXModel()).isEqualTo(translationNextY.getMaxXModel());
        assertThat(translation.getMaxYModel()).isEqualTo(translationNextX.getMaxYModel());
      }
    }
  }

  @Test
  public void test_0_0_zf_1() throws Exception {
    TileTranslation translation = new TileTranslation(0, 0, 1, 1, 200, 200);

    assertThat(translation.getMinXModel()).isEqualTo(0);
    assertThat(translation.getMaxXModel()).isEqualTo(200);
    assertThat(translation.getMinYModel()).isEqualTo(0);
    assertThat(translation.getMaxYModel()).isEqualTo(200);
    checkPlausibility(translation);

    assertThat(translation.model2tileX(0)).isEqualTo(0);
    assertThat(translation.model2tileX(200)).isEqualTo(200);
    assertThat(translation.model2tileY(0)).isEqualTo(0);
    assertThat(translation.model2tileY(200)).isEqualTo(200);
  }

  @Test
  public void test_0_0_zf_2() throws Exception {
    TileTranslation translation = new TileTranslation(0, 0, 2, 2, 200, 200);

    assertThat(translation.getMinXModel()).isEqualTo(0);
    assertThat(translation.getMaxXModel()).isEqualTo(100);
    assertThat(translation.getMinYModel()).isEqualTo(0);
    assertThat(translation.getMaxYModel()).isEqualTo(100);
    checkPlausibility(translation);

    assertThat(translation.model2tileX(0)).isEqualTo(0);
    assertThat(translation.model2tileX(100)).isEqualTo(200);
    assertThat(translation.model2tileY(0)).isEqualTo(0);
    assertThat(translation.model2tileY(100)).isEqualTo(200);
  }

  @Test
  public void test_1_2_zf_2() throws Exception {
    TileTranslation translation = new TileTranslation(1, 2, 2, 2, 200, 200);

    assertThat(translation.getMinXModel()).isEqualTo(100);
    assertThat(translation.getMaxXModel()).isEqualTo(200);
    assertThat(translation.getMinYModel()).isEqualTo(200);
    assertThat(translation.getMaxYModel()).isEqualTo(300);
    checkPlausibility(translation);

    assertThat(translation.model2tileX(100)).isEqualTo(0);
    assertThat(translation.model2tileX(200)).isEqualTo(200);
    assertThat(translation.model2tileY(200)).isEqualTo(0);
    assertThat(translation.model2tileY(300)).isEqualTo(200);
  }

  @Test
  public void test_n1_n2_zf_05() throws Exception {
    TileTranslation translation = new TileTranslation(-1, -2, 0.5, 0.5, 200, 200);

    assertThat(translation.getMinXModel()).isEqualTo(-400);
    assertThat(translation.getMaxXModel()).isEqualTo(0);
    assertThat(translation.getMinYModel()).isEqualTo(-800);
    assertThat(translation.getMaxYModel()).isEqualTo(-400);
    checkPlausibility(translation);

    assertThat(translation.model2tileX(-400)).isEqualTo(0);
    assertThat(translation.model2tileX(0)).isEqualTo(200);
    assertThat(translation.model2tileY(-800)).isEqualTo(0);
    assertThat(translation.model2tileY(-400)).isEqualTo(200);
  }

  private void checkPlausibility(@Nonnull TileTranslation translation) {
    assertThat(translation.model2tileX(translation.getMinXModel())).isEqualTo(0);
    assertThat(translation.model2tileX(translation.getMaxXModel())).isEqualTo(translation.getTileWidth());
    assertThat(translation.model2tileY(translation.getMinYModel())).isEqualTo(0);
    assertThat(translation.model2tileY(translation.getMaxYModel())).isEqualTo(translation.getTileHeight());

    //Check center
    assertThat(translation.model2tileX(translation.getCenterXModel())).isEqualTo(translation.getTileWidth()/2.0);
    assertThat(translation.model2tileY(translation.getCenterYModel())).isEqualTo(translation.getTileHeight()/2.0);

    //Check reverse
    assertThat(translation.tile2modelX(0)).isEqualTo(translation.getMinXModel());
    assertThat(translation.tile2modelX(translation.getTileWidth())).isEqualTo(translation.getMaxXModel());
    assertThat(translation.tile2modelY(0)).isEqualTo(translation.getMinYModel());
    assertThat(translation.tile2modelY(translation.getTileHeight())).isEqualTo(translation.getMaxYModel());

    //In model range
    assertThat(translation.isInModelXRange(translation.tile2modelX(0))).isTrue();
    assertThat(translation.isInModelXRange(translation.tile2modelX(translation.getTileWidth()))).isTrue();
    assertThat(translation.isInModelXRange(translation.tile2modelX(0 - 1))).isFalse();
    assertThat(translation.isInModelXRange(translation.tile2modelX(translation.getTileWidth() + 1))).isFalse();

    assertThat(translation.isInModelYRange(translation.tile2modelY(0))).isTrue();
    assertThat(translation.isInModelYRange(translation.tile2modelY(translation.getTileWidth()))).isTrue();
    assertThat(translation.isInModelYRange(translation.tile2modelY(0 - 1))).isFalse();
    assertThat(translation.isInModelYRange(translation.tile2modelY(translation.getTileWidth() + 1))).isFalse();


    //Test translations
    Line2D.Double line = new Line2D.Double(translation.getMinXModel(), translation.getMinYModel(), translation.getMaxXModel(), translation.getMaxYModel());
    Line2D.Double modifiedLine = translation.transformToTile(line);
    assertThat(line).isSameAs(modifiedLine);

    assertThat(modifiedLine.getX1()).isEqualTo(0);
    assertThat(modifiedLine.getY1()).isEqualTo(0);

    assertThat(modifiedLine.getX2()).isEqualTo(translation.getTileWidth());
    assertThat(modifiedLine.getY2()).isEqualTo(translation.getTileHeight());
  }
}