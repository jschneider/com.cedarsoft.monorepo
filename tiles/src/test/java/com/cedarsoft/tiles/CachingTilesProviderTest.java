package com.cedarsoft.tiles;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.*;

import javax.annotation.Nonnull;
import java.awt.Image;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CachingTilesProviderTest {
  @Test
  public void testCacheDeleteExpire() throws Exception {
    LoadingCache<String, String> cache = CacheBuilder.<TileLocation, Image>newBuilder()
      .recordStats()
      .maximumSize(2)
      .build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
          return key;
        }
      });

    assertThat(cache.get("asdf")).isEqualTo("asdf");
    assertThat(cache.get("222")).isEqualTo("222");
    assertThat(cache.size()).isEqualTo(2);
    assertThat(cache.get("333")).isEqualTo("333");
    assertThat(cache.size()).isEqualTo(2);

    assertThat(cache.getIfPresent("333")).isEqualTo("333");
    assertThat(cache.getIfPresent("222")).isEqualTo("222");
    assertThat(cache.getIfPresent("asdf")).isNull();

    assertThat(cache.stats().missCount()).isEqualTo(4);
    assertThat(cache.stats().hitCount()).isEqualTo(2);
  }

  @Test
  @Ignore
  public void testRefresh() throws Exception {
    LoadingCache<Integer, String> cache = CacheBuilder.<Integer, String>newBuilder()
      .maximumSize(10)
      .build(new CacheLoader<Integer, String>() {
        @Override
        public String load(@Nonnull Integer key) throws Exception {
          Thread.sleep(400);
          return String.valueOf(key);
        }
      });


    assertThat(cache.size()).isEqualTo(0);
    assertThat(cache.getIfPresent(123)).isNull();

    {
      Stopwatch stopwatch = Stopwatch.createStarted();
      assertThat(cache.get(123)).isEqualTo("123");
      assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThanOrEqualTo(400);
    }

    {
      Stopwatch stopwatch = Stopwatch.createStarted();
      cache.refresh(123);
      assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThanOrEqualTo(400);
    }

    {
      Stopwatch stopwatch = Stopwatch.createStarted();
      assertThat(cache.get(123)).isEqualTo("123");
      assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isLessThan(200);
    }

    {
      Stopwatch stopwatch = Stopwatch.createStarted();
      cache.refresh(9999);
      assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThanOrEqualTo(400);
    }

    {
      Stopwatch stopwatch = Stopwatch.createStarted();
      assertThat(cache.get(9999)).isEqualTo("9999");
      assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isLessThan(200);
    }
  }
}