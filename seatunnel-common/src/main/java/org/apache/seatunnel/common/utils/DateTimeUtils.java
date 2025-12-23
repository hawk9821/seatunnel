/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.common.utils;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

public class DateTimeUtils {

    private static final Map<Formatter, DateTimeFormatter> FORMATTER_MAP = new HashMap<>();

    static {
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_SSSSSS,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_SSSSSS.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_SPOT,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_SPOT.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_SLASH,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_SLASH.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_M_D_HH_MM_SS_SLASH,
                DateTimeFormatter.ofPattern(Formatter.YYYY_M_D_HH_MM_SS_SLASH.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_M_D_HH_MM_SS_ISO8601,
                DateTimeFormatter.ofPattern(Formatter.YYYY_M_D_HH_MM_SS_ISO8601.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_M_D_HH_MM_SLASH,
                DateTimeFormatter.ofPattern(Formatter.YYYY_M_D_HH_MM_SLASH.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_M_D_HH_MM_ISO8601,
                DateTimeFormatter.ofPattern(Formatter.YYYY_M_D_HH_MM_ISO8601.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_NO_SPLIT,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_NO_SPLIT.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_ISO8601,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_ISO8601.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_SSS_ISO8601,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_SSS_ISO8601.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_SSSSSS_ISO8601,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_SSSSSS_ISO8601.value));
        FORMATTER_MAP.put(
                Formatter.YYYY_MM_DD_HH_MM_SS_SSSSSSSSS_ISO8601,
                DateTimeFormatter.ofPattern(Formatter.YYYY_MM_DD_HH_MM_SS_SSSSSSSSS_ISO8601.value));
    }

    // Define date-time format pattern, containing regex and corresponding formatter
    private static class DateTimePattern {
        final Pattern pattern;
        final DateTimeFormatter formatter;

        DateTimePattern(String regex, DateTimeFormatter formatter) {
            this.pattern = Pattern.compile(regex);
            this.formatter = formatter;
        }

        DateTimePattern(String regex, String format) {
            this.pattern = Pattern.compile(regex);
            this.formatter = DateTimeFormatter.ofPattern(format);
        }
    }

    // List of date-time format patterns, sorted by priority
    private static final List<DateTimePattern> PATTERN_LIST = new ArrayList<>();

    static {
        // Initialize date-time format patterns - most common formats first
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}",
                        Formatter.YYYY_MM_DD_HH_MM_SS.value)); // Most common: yyyy-MM-dd HH:mm:ss
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}",
                        Formatter.YYYY_MM_DD_HH_MM_SS_ISO8601.value)); // ISO8601 format
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2}:\\d{2}",
                        Formatter.YYYY_MM_DD_HH_MM_SS_SLASH.value)); // Slash format
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}\\.\\d{2}\\.\\d{2}\\s\\d{2}:\\d{2}:\\d{2}",
                        Formatter.YYYY_MM_DD_HH_MM_SS_SPOT.value)); // Dot format
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{14}",
                        Formatter.YYYY_MM_DD_HH_MM_SS_NO_SPLIT.value)); // 14-digit format
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}",
                        Formatter.YYYY_M_D_HH_MM_SS_ISO8601
                                .value)); // Single-digit month/day (dash)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}",
                        Formatter.YYYY_M_D_HH_MM_SS_SLASH.value)); // Single-digit month/day (slash)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}",
                        Formatter.YYYY_M_D_HH_MM_ISO8601
                                .value)); // No seconds (single-digit month/day)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{2}:\\d{2}",
                        Formatter.YYYY_M_D_HH_MM_SLASH
                                .value)); // No seconds (single-digit month/day)

        // With milliseconds formats
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}.*",
                        new DateTimeFormatterBuilder()
                                .parseCaseInsensitive()
                                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                                .appendLiteral(' ')
                                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                                .toFormatter())); // With milliseconds (dash)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}.*",
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // With milliseconds (ISO8601)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2}.*",
                        new DateTimeFormatterBuilder()
                                .parseCaseInsensitive()
                                .append(
                                        new DateTimeFormatterBuilder()
                                                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                                                .appendLiteral('/')
                                                .appendValue(MONTH_OF_YEAR, 2)
                                                .appendLiteral('/')
                                                .appendValue(DAY_OF_MONTH, 2)
                                                .toFormatter())
                                .appendLiteral(' ')
                                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                                .toFormatter())); // With milliseconds (slash)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}\\.\\d{2}\\.\\d{2}\\s\\d{2}:\\d{2}.*",
                        new DateTimeFormatterBuilder()
                                .parseCaseInsensitive()
                                .append(
                                        new DateTimeFormatterBuilder()
                                                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                                                .appendLiteral('.')
                                                .appendValue(MONTH_OF_YEAR, 2)
                                                .appendLiteral('.')
                                                .appendValue(DAY_OF_MONTH, 2)
                                                .toFormatter())
                                .appendLiteral(' ')
                                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                                .toFormatter())); // With milliseconds (dot)
        PATTERN_LIST.add(
                new DateTimePattern(
                        "\\d{4}年\\d{2}月\\d{2}日\\s\\d{2}时\\d{2}分\\d{2}秒",
                        "yyyy年MM月dd日 HH时mm分ss秒")); // Chinese format
    }

    /**
     * Match the corresponding DateTimeFormatter based on the date-time string
     *
     * @param dateTime Date-time string, e.g.: 2020-02-03 12:12:10.101
     * @return Matched DateTimeFormatter, or null if no pattern matches
     */
    public static DateTimeFormatter matchDateTimeFormatter(String dateTime) {
        for (DateTimePattern pattern : PATTERN_LIST) {
            if (pattern.pattern.matcher(dateTime).matches()) {
                return pattern.formatter;
            }
        }
        return null;
    }

    /**
     * Parse date-time string using the specified DateTimeFormatter
     *
     * @param dateTime Date-time string
     * @param dateTimeFormatter Date-time formatter
     * @return Parsed LocalDateTime object
     */
    public static LocalDateTime parse(String dateTime, DateTimeFormatter dateTimeFormatter) {
        TemporalAccessor parsedTimestamp = dateTimeFormatter.parse(dateTime);
        LocalTime localTime = parsedTimestamp.query(TemporalQueries.localTime());
        LocalDate localDate = parsedTimestamp.query(TemporalQueries.localDate());
        return LocalDateTime.of(localDate, localTime);
    }

    /**
     * Automatically infer date-time string format and parse
     *
     * <p>Note: There is a certain performance loss due to the need to determine the formatter rules
     * through regular expressions. Tested on 8c16g macOS, compared with directly passing the
     * formatter, the most obvious performance degradation is
     * 'Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2}\\s\\d{2}:\\d{2}.*")' in a scenario of 10 million
     * calculations, increasing from 4.5 seconds to 10 seconds.
     *
     * <p>Analysis shows there are two main reasons: First, the position of this regular expression
     * in the mapping is 4, and three regular expression matches are needed before it; Second, in
     * order to support non-fixed millisecond digits (minimum 0 digits, maximum 9 digits), we use
     * {@link DateTimeFormatter#ISO_LOCAL_TIME}, which also increases the time for time conversion.
     *
     * @param dateTime Date-time string, e.g.: 2020-02-03 12:12:10.101
     * @return Parsed LocalDateTime object
     */
    public static LocalDateTime parse(String dateTime) {
        DateTimeFormatter dateTimeFormatter = matchDateTimeFormatter(dateTime);
        if (dateTimeFormatter == null) {
            throw new IllegalArgumentException("Unsupported datetime format: " + dateTime);
        }
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    /**
     * Parse date-time string using the specified Formatter enum
     *
     * @param dateTime Date-time string
     * @param formatter Date-time format enum
     * @return Parsed LocalDateTime object
     */
    public static LocalDateTime parse(String dateTime, Formatter formatter) {
        return LocalDateTime.parse(dateTime, FORMATTER_MAP.get(formatter));
    }

    /**
     * Parse date-time string using the specified format string
     *
     * @param dateTime Date-time string
     * @param format Date-time format string, e.g.: yyyy-MM-dd HH:mm:ss
     * @return Parsed LocalDateTime object
     */
    public static LocalDateTime parse(String dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    public static LocalDateTime parse(long timestamp) {
        return parse(timestamp, ZoneId.systemDefault());
    }

    public static LocalDateTime parse(long timestamp, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Format LocalDateTime to string with specified format
     *
     * @param dateTime Date-time object
     * @param formatter Date-time format enum
     * @return Formatted string
     */
    public static String toString(LocalDateTime dateTime, Formatter formatter) {
        return dateTime.format(FORMATTER_MAP.get(formatter));
    }

    /**
     * Format LocalDateTime to string with specified format string
     *
     * @param dateTime Date-time object
     * @param format Date-time format string, e.g.: yyyy-MM-dd HH:mm:ss
     * @return Formatted string
     */
    public static String toString(LocalDateTime dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(dateTimeFormatter);
    }

    /**
     * Format OffsetDateTime to string with specified format
     *
     * @param offsetDateTime Offset date-time object
     * @param formatter Date-time format enum
     * @return Formatted string
     */
    public static String toString(OffsetDateTime offsetDateTime, Formatter formatter) {
        return toString(offsetDateTime.toLocalDateTime(), formatter);
    }

    /**
     * Format Temporal object to string with specified format
     *
     * @param temporal Date-time object
     * @param formatter Date-time format enum
     * @return Formatted string
     */
    public static String toString(Temporal temporal, Formatter formatter) {
        if (temporal instanceof OffsetDateTime) {
            return toString(((OffsetDateTime) temporal).toLocalDateTime(), formatter);
        } else if (temporal instanceof java.time.ZonedDateTime) {
            return toString(((java.time.ZonedDateTime) temporal).toLocalDateTime(), formatter);
        } else {
            return FORMATTER_MAP.get(formatter).format(temporal);
        }
    }

    /**
     * Format timestamp to string with specified format
     *
     * @param timestamp Timestamp in milliseconds
     * @param formatter Date-time format enum
     * @return Formatted string
     */
    public static String toString(long timestamp, Formatter formatter) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return toString(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()), formatter);
    }

    /**
     * Format timestamp to string with specified format string
     *
     * @param timestamp Timestamp in milliseconds
     * @param format Date-time format string, e.g.: yyyy-MM-dd HH:mm:ss
     * @return Formatted string
     */
    public static String toString(long timestamp, String format) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return toString(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()), format);
    }

    @Getter
    public enum Formatter implements org.apache.seatunnel.common.config.Formatter<Formatter> {
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_SS_SSSSSS("yyyy-MM-dd HH:mm:ss.SSSSSS"),
        YYYY_MM_DD_HH_MM_SS_SPOT("yyyy.MM.dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_SS_SLASH("yyyy/MM/dd HH:mm:ss"),
        YYYY_M_D_HH_MM_SLASH("yyyy/M/d HH:mm"),
        YYYY_M_D_HH_MM_ISO8601("yyyy-M-d HH:mm"),
        YYYY_M_D_HH_MM_SS_SLASH("yyyy/M/d HH:mm:ss"),
        YYYY_M_D_HH_MM_SS_ISO8601("yyyy-M-d HH:mm:ss"),
        YYYY_MM_DD_HH_MM_SS_NO_SPLIT("yyyyMMddHHmmss"),
        YYYY_MM_DD_HH_MM_SS_ISO8601("yyyy-MM-dd'T'HH:mm:ss"),
        YYYY_MM_DD_HH_MM_SS_SSS_ISO8601("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        YYYY_MM_DD_HH_MM_SS_SSSSSS_ISO8601("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
        YYYY_MM_DD_HH_MM_SS_SSSSSSSSS_ISO8601("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

        private final String value;

        Formatter(String value) {
            this.value = value;
        }

        public static Formatter parse(String format) {
            Formatter[] formatters = Formatter.values();
            for (Formatter formatter : formatters) {
                if (formatter.getValue().equals(format)) {
                    return formatter;
                }
            }
            String errorMsg = String.format("Illegal format [%s]", format);
            throw new IllegalArgumentException(errorMsg);
        }

        @Override
        public Formatter getFormatter() {
            return this;
        }
    }
}
