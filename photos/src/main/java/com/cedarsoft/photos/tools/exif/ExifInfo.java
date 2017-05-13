/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.photos.tools.exif;

import com.cedarsoft.exceptions.NotFoundException;
import com.cedarsoft.image.Resolution;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Simple exif parser
 */
@Immutable
public class ExifInfo {
  private static final Logger LOG = Logger.getLogger(ExifInfo.class.getName());

  @Nonnull
  public static final ExifInfo UNKNOWN = new ExifInfo();

  @Nonnull
  private final ImmutableMap<String, Entry> entries;
  @Nonnull
  public static final String UNIT_MM = "mm";

  private ExifInfo() {
    entries = ImmutableMap.of();
  }

  public ExifInfo(@Nonnull InputStream exifIn) throws IOException {
    List<String> lines = IOUtils.readLines(exifIn, Charset.defaultCharset());

    Map<String, Entry> entriesBuilder = new HashMap<>();

    for (String line : lines) {
      if (line.trim().isEmpty()) {
        continue;
      }

      Entry entry = Entry.parse(line);
      if (!entriesBuilder.containsKey(entry.getKey())) {
        entriesBuilder.put(entry.getKey(), entry);
      }
      else {
        LOG.fine("WARNING: Duplicate key <" + entry.getKey() + ">");
      }
    }

    entries = ImmutableMap.copyOf(entriesBuilder);
  }

  @Nonnull
  public Map<String, Entry> getEntries() {
    //noinspection ReturnOfCollectionOrArrayField
    return entries;
  }

  /**
   * Returns null if there is no capture time in the exif data
   */
  @Nullable
  public ZonedDateTime getCaptureTimeNullable(@Nonnull ZoneId fallbackCaptureZoneId) {
    try {
      return getCaptureTime(fallbackCaptureZoneId);
    } catch (NotFoundException ignore) {
    }

    return null;
  }


  /**
   * Returns the capture time.
   * It is necessary to add a time zone since that is not stored within the exif data
   *
   * @param fallbackCaptureZoneId the date time zone that is used to calculate the date. Only used if there is no time zone setting in the EXIF
   * @return the capture time
   */
  @Nonnull
  public ZonedDateTime getCaptureTime(@Nonnull ZoneId fallbackCaptureZoneId) throws NotFoundException {
    Object createDateValue = findEntry("CreateDate").getValue();
    if (createDateValue == null) {
      throw new IllegalArgumentException("No CreateDate found");
    }

    DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
      .appendValue(ChronoField.YEAR)
      .appendLiteral(':')
      .appendValue(ChronoField.MONTH_OF_YEAR)
      .appendLiteral(':')
      .appendValue(ChronoField.DAY_OF_MONTH)
      .appendLiteral(' ')
      .appendValue(ChronoField.HOUR_OF_DAY)
      .appendLiteral(':')
      .appendValue(ChronoField.MINUTE_OF_HOUR)
      .appendLiteral(':')
      .appendValue(ChronoField.SECOND_OF_MINUTE)
      .toFormatter();


    ZoneId relevantZone;
    try {
      Object timeZoneValue = findEntry("TimeZone").getValue();
      relevantZone = ZoneId.of(String.valueOf(timeZoneValue));
    } catch (NotFoundException ignore) {
      relevantZone = fallbackCaptureZoneId;
    }

    TemporalAccessor temporalAccessor = timeFormatter.withZone(relevantZone).parse(String.valueOf(createDateValue));
    return ZonedDateTime.from(temporalAccessor);
  }

  @Nonnull
  private Entry findEntry(@Nonnull String... keys) throws NotFoundException {
    if (keys.length == 0) {
      throw new IllegalArgumentException("Need at least one key");
    }

    for (String key : keys) {
      @Nullable Entry found = entries.get(key);
      if (found != null) {
        return found;
      }
    }

    throw new NotFoundException("No entry found for key <" + Arrays.toString(keys) + ">");
  }

  //todo check with nikon etc.

  public int getFileNumber() throws NotFoundException {
    Object value = findEntry("FileNumber").getValueNonNull();

    Iterator<String> parts = Splitter.on("-").split(String.valueOf(value)).iterator();
    String firstPart = parts.next();
    String secondPart = parts.next();

    return ((Integer.parseInt(firstPart) - 100) * 10000) + Integer.parseInt(secondPart);
  }

  /**
   * Return 0 if there is no file number
   */
  public int getFileNumberSafe() {
    try {
      return getFileNumber();
    } catch (NotFoundException ignore) {
      return 0;
    }
  }

  @Nonnull

  public String getModel() throws NotFoundException {
    return String.valueOf(findEntry("Model").getValue());
  }

  /**
   * The make of the camera
   *
   * @return the make
   */
  @Nonnull
  public String getMake() throws NotFoundException {
    return String.valueOf(findEntry("Make").getValue());
  }

  @Nonnull
  public String getLensType() throws NotFoundException {
    return String.valueOf(findEntry("LensType").getValue());
  }

  @Nonnull
  public String getFileTypeExtension() throws NotFoundException {
    return String.valueOf(findEntry("FileTypeExtension").getValue());
  }

  /**
   * Returns a camera identifier (as specific as possible)
   */
  @Nonnull
  public String getCameraId() {
    try {
      return String.valueOf(getCameraSerial());
    } catch (NotFoundException ignore) {
    }

    //Fallback if there is no serial number
    return getCameraInfo().getModel().replace(" ", "_");
  }

  @Nonnull
  public String getCameraSerial() throws NotFoundException {
    Object value = findEntry("SerialNumber").getValueNonNull();
    return String.valueOf(value);
  }

  @Nonnull
  public String getCameraSerialSafe() {
    try {
      return getCameraSerial();
    } catch (NotFoundException ignore) {
      return "";
    }
  }

  @Nonnull
  public String getInternalSerialSafe() {
    try {
      return getInternalSerial();
    } catch (NotFoundException ignore) {
      return "";
    }
  }

  @Nonnull
  public String getInternalSerial() throws NotFoundException {
    Object value = findEntry("InternalSerialNumber").getValueNonNull();
    return String.valueOf(value);
  }

  public int getMinFocalLength() throws NotFoundException {
    return parseLength(String.valueOf(findEntry("MinFocalLength", "ShortFocal").getValueNonNull()));
  }

  public int getMaxFocalLength() throws NotFoundException {
    return parseLength(String.valueOf(findEntry("MaxFocalLength", "LongFocal").getValueNonNull()));
  }

  public int getFocalLength() throws NotFoundException {
    return parseLength(String.valueOf(findEntry("FocalLength").getValueNonNull()));
  }

  private int parseLength(@Nonnull String value) {
    try {
      Iterator<String> parts = Splitter.on(" ").split(value).iterator();

      String lengthAsString = parts.next();
      String unit = parts.next();
      //noinspection CallToStringEquals
      if (!unit.equals(UNIT_MM)) {
        throw new IllegalArgumentException("Invalid unit: " + unit);
      }

      return Double.valueOf(Double.parseDouble(lengthAsString)).intValue();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid length: <" + value + ">", e);
    }
  }

  /**
   * Extracts the camera info
   */
  @Nonnull
  public CameraInfo getCameraInfo() throws NotFoundException {
    return new CameraInfo(getCameraSerialSafe(), getMake(), getModel(), getInternalSerialSafe());
  }

  public double getMaxAperture() throws NotFoundException {
    return Double.parseDouble(String.valueOf(findEntry("MaxAperture").getValueNonNull()));
  }

  public double getMinAperture() throws NotFoundException {
    return Double.parseDouble(String.valueOf(findEntry("MinAperture").getValueNonNull()));
  }

  public double getAperture() throws NotFoundException {
    return Double.parseDouble(String.valueOf(findEntry("FNumber").getValueNonNull()));
  }

  @Nonnull
  private static final Pattern PATTERN = Pattern.compile("/");

  @Nonnull

  public String getExposureTimeFraction() throws NotFoundException {
    return String.valueOf(findEntry("ExposureTime").getValueNonNull());
  }

  public double getExposureTime() throws ParseException, NotFoundException {
    String exposureTime = String.valueOf(findEntry("ExposureTime").getValueNonNull());
    //String exposureTime = String.valueOf(findEntry("ShutterSpeedValue").getValueNonNull());

    String[] parts = PATTERN.split(exposureTime);
    if (parts.length != 2) {
      throw new ParseException(exposureTime, 0);
    }

    return 1.0 / Double.parseDouble(parts[1]);
  }

  public double getCropFactor() {
    return Double.parseDouble(String.valueOf(findEntry("ScaleFactor35efl").getValueNonNull()));
  }

  public int getIso() {
    return Integer.parseInt(String.valueOf(findEntry("ISO").getValueNonNull()));
  }

  @Nonnull
  public Resolution getDimension() {
    int width = Integer.parseInt(String.valueOf(findEntry("ImageWidth").getValueNonNull()));
    int height = Integer.parseInt(String.valueOf(findEntry("ImageHeight").getValueNonNull()));

    return new Resolution(width, height);
  }

  @Nonnull
  public String getOrientation() {
    return String.valueOf(findEntry("Orientation").getValueNonNull());
  }

  @Override
  public String toString() {
    return "ExifInfo{" +
      "entries=" + entries +
      '}';
  }

  //  public int getHyperfocalDistance() {
  //    return findEntry( "HyperfocalDistance" );
  //  }
  //
  //  public int getCircleOfConfusion() {
  //    return findEntry( "CircleOfConfusion" );
  //  }

  @Immutable
  public static class Entry {
    private final int id;
    @Nonnull
    private final String key;

    @Nullable
    private final String value;

    private Entry(int id, @Nonnull String key, @Nullable String value) {
      this.id = id;
      this.key = key;
      this.value = value;
    }

    public int getId() {
      return id;
    }

    @Nonnull
    public String getKey() {
      return key;
    }

    @Nullable
    public String getValue() {
      return value;
    }

    @Nonnull
    public String getValueNonNull() {
      if (value == null) {
        throw new IllegalStateException("No value available for <" + id + ">");
      }
      return value;
    }

    @Override
    public String toString() {
      return "Entry{" +
        "id=" + id +
        ", key='" + key + '\'' +
        ", value=" + value +
        '}';
    }

    @Nonnull
    public static Entry parse(@Nonnull String line) {
      Iterator<String> parts = Splitter.on("\t").split(line).iterator();

      int id;

      String idAsString = parts.next();
      try {
        id = Integer.parseInt(idAsString);
      } catch (NumberFormatException ignore) {
        id = -1;
      }

      String key = parts.next();
      String value = null;
      try {
        value = parts.next();
      } catch (NoSuchElementException ignore) {
      }

      return new Entry(id, key, value);
    }
  }
}
