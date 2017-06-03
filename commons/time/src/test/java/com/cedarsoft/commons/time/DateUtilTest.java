package com.cedarsoft.commons.time;

import com.cedarsoft.test.utils.LocaleRule;
import org.junit.*;

import java.time.Duration;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DateUtilTest {
  @Rule
  public LocaleRule localeRule = new LocaleRule(Locale.GERMAN);

  @Test
  public void humanReadableDuration() throws Exception {
    assertThat(DateUtil.toHumanReadableDuration(1000)).isEqualTo("1 second");
    assertThat(DateUtil.toHumanReadableDuration(1000 + 1000 * 61)).isEqualTo("1 minute 2 seconds");
  }

  @Test
  public void duration() throws Exception {
    assertThat(DateUtil.formatHMSM(1000 + 1000 * 61)).isEqualTo("00:01:02.000");
    assertThat(DateUtil.formatDurationWords(1000 + 1000 * 61)).isEqualTo("1min 02s");

    assertThat(DateUtil.formatDurationWords(Duration.ofDays(3).toMillis())).isEqualTo("72h 00min 00s");
    assertThat(DateUtil.formatDurationWords(Duration.ofDays(3).toMillis() + 1000)).isEqualTo("72h 00min 01s");
    assertThat(DateUtil.formatDurationWords(Duration.ofDays(13).toMillis() + 1000)).isEqualTo("312h 00min 01s");
    assertThat(DateUtil.formatDurationWords(Duration.ofMinutes(3).toMillis() + 1000)).isEqualTo("3min 01s");

    assertThat(DateUtil.formatDurationWords(Duration.ofSeconds(1).toMillis())).isEqualTo("1s");
    assertThat(DateUtil.formatDurationWords(Duration.ofMinutes(1).toMillis())).isEqualTo("1min 00s");
    assertThat(DateUtil.formatDurationWords(Duration.ofHours(1).toMillis())).isEqualTo("1h 00min 00s");
    assertThat(DateUtil.formatDurationWords(Duration.ofDays(1).toMillis())).isEqualTo("24h 00min 00s");
    Locale.setDefault(Locale.US);
    assertThat(DateUtil.formatDurationWords(Duration.ofDays(100).toMillis())).isEqualTo("2,400h 00min 00s");
    Locale.setDefault(Locale.GERMAN);
    assertThat(DateUtil.formatDurationWords(Duration.ofDays(100).toMillis())).isEqualTo("2.400h 00min 00s");
  }

  @Test
  public void formatHourMinute() throws Exception {
    assertThat(DateUtil.formatDurationHHmm(Duration.ofHours(1).plusMinutes(10))).isEqualTo("01:10");
    assertThat(DateUtil.formatDurationHHmm(Duration.ofHours(18).plusMinutes(10))).isEqualTo("18:10");
    assertThat(DateUtil.formatDurationHHmm(Duration.ofHours(118).plusMinutes(10))).isEqualTo("118:10");
  }
}
