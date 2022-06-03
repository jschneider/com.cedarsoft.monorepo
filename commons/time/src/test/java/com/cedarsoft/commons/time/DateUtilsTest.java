/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.commons.time;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.cedarsoft.test.utils.WithLocale;

/**
 */
@WithLocale("en_US")
public class DateUtilsTest {
  @Test
  public void humanReadableDuration() throws Exception {
    assertThat(DateUtils.formatDurationWords(1000)).isEqualTo("1 second");
    assertThat(DateUtils.formatDurationWords(1000 + 1000 * 61)).isEqualTo("1 minute 2 seconds");
  }

  @Test
  public void format() throws Exception {
    assertThat(DateUtils.formatDurationHHmmSSmmm(1000 + 1000 * 61)).isEqualTo("00:01:02.000");
  }

  @Test
  public void durationWorldsNotFollowing() throws Exception {
    assertThat(DateUtils.formatDurationWords(Duration.ofHours(1).plusMinutes(30))).isEqualTo("1 hour 30 minutes");
    assertThat(DateUtils.formatDurationWords(Duration.ofHours(1))).isEqualTo("1 hour");
  }

  @WithLocale("en_US")
  @Test
  public void duration() throws Exception {
    assertThat(DateUtils.formatDurationWordsWithSeconds(1000 + 1000 * 61)).isEqualTo("1min 02s");

    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofDays(3).toMillis())).isEqualTo("72h 00min 00s");
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofDays(3).toMillis() + 1000)).isEqualTo("72h 00min 01s");
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofDays(13).toMillis() + 1000)).isEqualTo("312h 00min 01s");
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofMinutes(3).toMillis() + 1000)).isEqualTo("3min 01s");

    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofSeconds(1).toMillis())).isEqualTo("1s");
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofMinutes(1).toMillis())).isEqualTo("1min 00s");
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofHours(1).toMillis())).isEqualTo("1h 00min 00s");
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofDays(1).toMillis())).isEqualTo("24h 00min 00s");
    Locale.setDefault(Locale.US);
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofDays(100).toMillis())).isEqualTo("2,400h 00min 00s");
    Locale.setDefault(Locale.GERMAN);
    assertThat(DateUtils.formatDurationWordsWithSeconds(Duration.ofDays(100).toMillis())).isEqualTo("2.400h 00min 00s");
  }

  @Test
  public void formatHourMinute() throws Exception {
    assertThat(DateUtils.formatDurationHHmm(Duration.ofHours(1).plusMinutes(10))).isEqualTo("01:10");
    assertThat(DateUtils.formatDurationHHmm(Duration.ofHours(18).plusMinutes(10))).isEqualTo("18:10");
    assertThat(DateUtils.formatDurationHHmm(Duration.ofHours(118).plusMinutes(10))).isEqualTo("118:10");

    String formatted = DateUtils.formatDurationHHmm(Duration.ofHours(118).plusMinutes(10));
    assertThat(DateUtils.parseDurationHHmm(formatted)).isEqualTo(Duration.ofHours(118).plusMinutes(10));
  }
}
