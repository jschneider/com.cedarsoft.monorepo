package com.cedarsoft.commons.time;

import com.cedarsoft.test.utils.LocaleRule;
import org.assertj.core.api.*;
import org.junit.*;
import org.junit.Assert;

import java.time.Duration;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DateUtilsGermanLocaleTest {
  @Rule
  public LocaleRule localeRule = new LocaleRule(Locale.GERMANY);

  @Test
  public void words() throws Exception {
    assertThat(DateUtils.formatDurationWords(Duration.ofHours(13).plusMinutes(10))).isEqualTo("13 Stunden 10 Minuten");
    assertThat(DateUtils.formatDurationWords(Duration.ofHours(1).plusMinutes(1).plusSeconds(1))).isEqualTo("1 Stunde 1 Minute 1 Sekunde");
  }
}