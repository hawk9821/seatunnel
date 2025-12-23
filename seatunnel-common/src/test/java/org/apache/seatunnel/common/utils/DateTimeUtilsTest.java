/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.seatunnel.common.utils;

import org.apache.seatunnel.common.utils.DateTimeUtils.Formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTimeUtilsTest {

    @Test
    public void testParseDateString() {
        final String datetime = "2023-12-22 00:00:00";
        LocalDateTime parse = DateTimeUtils.parse(datetime, Formatter.YYYY_MM_DD_HH_MM_SS);
        Assertions.assertEquals(0, parse.getMinute());
        Assertions.assertEquals(0, parse.getHour());
        Assertions.assertEquals(0, parse.getSecond());
        Assertions.assertEquals(22, parse.getDayOfMonth());
        Assertions.assertEquals(12, parse.getMonth().getValue());
        Assertions.assertEquals(2023, parse.getYear());
        Assertions.assertEquals(22, parse.getDayOfMonth());
    }

    @Test
    public void testParseTimestamp() {
        // 2023-12-22 12:55:20
        final long timestamp = 1703220920013L;
        LocalDateTime parse = DateTimeUtils.parse(timestamp, ZoneId.of("Asia/Shanghai"));

        Assertions.assertEquals(55, parse.getMinute());
        Assertions.assertEquals(12, parse.getHour());
        Assertions.assertEquals(20, parse.getSecond());
        Assertions.assertEquals(22, parse.getDayOfMonth());
        Assertions.assertEquals(12, parse.getMonth().getValue());
        Assertions.assertEquals(2023, parse.getYear());
        Assertions.assertEquals(22, parse.getDayOfMonth());
    }

    @Test
    public void testAutoDateTimeFormatter() {
        String datetimeStr = "2020-10-10 10:10:10";
        Assertions.assertEquals("2020-10-10T10:10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020-10-10T10:10:10";
        Assertions.assertEquals("2020-10-10T10:10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020/10/10 10:10:10";
        Assertions.assertEquals("2020-10-10T10:10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020/1/1 10:10";
        Assertions.assertEquals("2020-01-01T10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2024/12/2 10:10";
        Assertions.assertEquals("2024-12-02T10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2024/12/1 10:10";
        Assertions.assertEquals("2024-12-01T10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020年10月10日 10时10分10秒";
        Assertions.assertEquals("2020-10-10T10:10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020.10.10 10:10:10";
        Assertions.assertEquals("2020-10-10T10:10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "20201010101010";
        Assertions.assertEquals("2020-10-10T10:10:10", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020-10-10 10:10:10.201";
        Assertions.assertEquals(
                "2020-10-10T10:10:10.201", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020-10-10 10:10:10.201111";
        Assertions.assertEquals(
                "2020-10-10T10:10:10.201111", DateTimeUtils.parse(datetimeStr).toString());

        datetimeStr = "2020-10-10 10:10:10.201111001";
        Assertions.assertEquals(
                "2020-10-10T10:10:10.201111001", DateTimeUtils.parse(datetimeStr).toString());
    }

    @Test
    public void testMatchDateTimeFormatter() {
        String datetimeStr = "2020-10-10 10:10:10";
        Assertions.assertEquals(
                "2020-10-10T10:10:10",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020-10-10T10:10:10";
        Assertions.assertEquals(
                "2020-10-10T10:10:10",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020/10/10 10:10:10";
        Assertions.assertEquals(
                "2020-10-10T10:10:10",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020年10月10日 10时10分10秒";
        Assertions.assertEquals(
                "2020-10-10T10:10:10",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020.10.10 10:10:10";
        Assertions.assertEquals(
                "2020-10-10T10:10:10",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "20201010101010";
        Assertions.assertEquals(
                "2020-10-10T10:10:10",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020-10-10 10:10:10.201";
        Assertions.assertEquals(
                "2020-10-10T10:10:10.201",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020-10-10 10:10:10.201111";
        Assertions.assertEquals(
                "2020-10-10T10:10:10.201111",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());

        datetimeStr = "2020-10-10 10:10:10.201111001";
        Assertions.assertEquals(
                "2020-10-10T10:10:10.201111001",
                DateTimeUtils.parse(datetimeStr, DateTimeUtils.matchDateTimeFormatter(datetimeStr))
                        .toString());
    }

    @Test
    @Deprecated
    public void testPerformance() {
        String datetimeStr = "2020-10-10 10:10:10";
        DateTimeFormatter dateTimeFormatter = DateTimeUtils.matchDateTimeFormatter(datetimeStr);
        String datetimeStr1 = "20201010101010";
        DateTimeFormatter dateTimeFormatter1 = DateTimeUtils.matchDateTimeFormatter(datetimeStr1);
        String datetimeStr2 = "2020.10.10 10:10:10.100";
        DateTimeFormatter dateTimeFormatter2 = DateTimeUtils.matchDateTimeFormatter(datetimeStr2);
        String datetimeStr3 = "2020.10.10 10:10:10";
        DateTimeFormatter dateTimeFormatter3 = DateTimeUtils.matchDateTimeFormatter(datetimeStr3);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr, dateTimeFormatter);
        }
        long t2 = System.currentTimeMillis();
        // Use an explicit time format 'yyyy-MM-dd HH:mm:ss' for processing, use time: 4552ms
        System.out.println((t2 - t1) + "");

        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr);
        }
        long t3 = System.currentTimeMillis();
        // If format is not specified, the system automatically obtains the format 'yyyy-MM-dd
        // HH:mm:ss' for processing, use time: 6082ms
        System.out.println((t3 - t2) + "");

        long t4 = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr1, dateTimeFormatter1);
        }
        long t5 = System.currentTimeMillis();
        // Use an explicit time format 'yyyyMMddHHmmss' for processing, use time: 4610ms
        System.out.println((t5 - t4) + "");

        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr1);
        }
        long t6 = System.currentTimeMillis();
        // If format is not specified, the system automatically obtains the format 'yyyyMMddHHmmss'
        // for processing, use time: 4842ms

        System.out.println((t6 - t5) + "");

        long t7 = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr2, dateTimeFormatter2);
        }
        long t8 = System.currentTimeMillis();
        // Use an explicit time format 'yyyy.MM.dd HH:mm:ss.SSS' for processing, use time: 8162ms
        System.out.println((t8 - t7) + "");

        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr2);
        }
        long t9 = System.currentTimeMillis();
        // If format is not specified, the system automatically obtains the format 'yyyy.MM.dd
        // HH:mm:ss.SSS' for processing, use time: 11366ms
        System.out.println((t9 - t8) + "");

        long t10 = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr3, dateTimeFormatter3);
        }
        long t11 = System.currentTimeMillis();
        // Use an explicit time format 'yyyy.MM.dd HH:mm:ss' for processing, use time: 4405ms
        System.out.println((t11 - t10) + "");

        for (int i = 0; i < 10000000; i++) {
            DateTimeUtils.parse(datetimeStr3);
        }
        long t12 = System.currentTimeMillis();
        // If format is not specified, the system automatically obtains the format 'yyyy.MM.dd
        // HH:mm:ss' for processing, use time: 7771ms
        System.out.println((t12 - t11) + "");
    }

    @Test
    public void testParseWithAutoFormat() {
        // Test auto-detecting date time format
        // 1. Basic formats
        LocalDateTime dateTime1 = DateTimeUtils.parse("2023-12-25 15:30:45");
        assertEquals(2023, dateTime1.getYear());
        assertEquals(12, dateTime1.getMonthValue());
        assertEquals(25, dateTime1.getDayOfMonth());
        assertEquals(15, dateTime1.getHour());
        assertEquals(30, dateTime1.getMinute());
        assertEquals(45, dateTime1.getSecond());

        // 2. No split format (14 digits)
        LocalDateTime dateTime2 = DateTimeUtils.parse("20231225153045");
        assertEquals(2023, dateTime2.getYear());
        assertEquals(12, dateTime2.getMonthValue());
        assertEquals(25, dateTime2.getDayOfMonth());
        assertEquals(15, dateTime2.getHour());
        assertEquals(30, dateTime2.getMinute());
        assertEquals(45, dateTime2.getSecond());

        // 3. Slash format
        LocalDateTime dateTime3 = DateTimeUtils.parse("2023/12/25 15:30:45");
        assertEquals(2023, dateTime3.getYear());
        assertEquals(12, dateTime3.getMonthValue());
        assertEquals(25, dateTime3.getDayOfMonth());

        // 4. Dot format
        LocalDateTime dateTime4 = DateTimeUtils.parse("2023.12.25 15:30:45");
        assertEquals(2023, dateTime4.getYear());
        assertEquals(12, dateTime4.getMonthValue());
        assertEquals(25, dateTime4.getDayOfMonth());

        // 5. ISO8601 format
        LocalDateTime dateTime5 = DateTimeUtils.parse("2023-12-25T15:30:45");
        assertEquals(2023, dateTime5.getYear());
        assertEquals(12, dateTime5.getMonthValue());
        assertEquals(25, dateTime5.getDayOfMonth());

        // 6. Single-digit month and day (ISO8601 style)
        LocalDateTime dateTime6 = DateTimeUtils.parse("2023-1-5 15:30:45");
        assertEquals(2023, dateTime6.getYear());
        assertEquals(1, dateTime6.getMonthValue());
        assertEquals(5, dateTime6.getDayOfMonth());

        // 7. Single-digit month and day (slash style)
        LocalDateTime dateTime7 = DateTimeUtils.parse("2023/1/5 15:30:45");
        assertEquals(2023, dateTime7.getYear());
        assertEquals(1, dateTime7.getMonthValue());
        assertEquals(5, dateTime7.getDayOfMonth());

        // 8. No seconds (ISO8601 style with single-digit)
        LocalDateTime dateTime8 = DateTimeUtils.parse("2023-1-5 15:30");
        assertEquals(2023, dateTime8.getYear());
        assertEquals(1, dateTime8.getMonthValue());
        assertEquals(5, dateTime8.getDayOfMonth());
        assertEquals(15, dateTime8.getHour());
        assertEquals(30, dateTime8.getMinute());
        assertEquals(0, dateTime8.getSecond());

        // 9. No seconds (ISO8601 style with double-digit)
        LocalDateTime dateTime9 = DateTimeUtils.parse("2023-12-25 15:30");
        assertEquals(2023, dateTime9.getYear());
        assertEquals(12, dateTime9.getMonthValue());
        assertEquals(25, dateTime9.getDayOfMonth());
        assertEquals(15, dateTime9.getHour());
        assertEquals(30, dateTime9.getMinute());
        assertEquals(0, dateTime9.getSecond());

        // 10. No seconds (slash style with single-digit)
        LocalDateTime dateTime10 = DateTimeUtils.parse("2023/1/5 15:30");
        assertEquals(2023, dateTime10.getYear());
        assertEquals(1, dateTime10.getMonthValue());
        assertEquals(5, dateTime10.getDayOfMonth());
        assertEquals(15, dateTime10.getHour());
        assertEquals(30, dateTime10.getMinute());
        assertEquals(0, dateTime10.getSecond());

        // 11. With milliseconds - dash format
        LocalDateTime dateTime11 = DateTimeUtils.parse("2023-12-25 15:30:45.123");
        assertEquals(2023, dateTime11.getYear());
        assertEquals(12, dateTime11.getMonthValue());
        assertEquals(25, dateTime11.getDayOfMonth());
        assertEquals(15, dateTime11.getHour());
        assertEquals(30, dateTime11.getMinute());
        assertEquals(45, dateTime11.getSecond());
        assertEquals(123000000, dateTime11.getNano());

        // 12. With milliseconds - ISO8601 format
        LocalDateTime dateTime12 = DateTimeUtils.parse("2023-12-25T15:30:45.123");
        assertEquals(2023, dateTime12.getYear());
        assertEquals(12, dateTime12.getMonthValue());
        assertEquals(25, dateTime12.getDayOfMonth());
        assertEquals(15, dateTime12.getHour());
        assertEquals(30, dateTime12.getMinute());
        assertEquals(45, dateTime12.getSecond());
        assertEquals(123000000, dateTime12.getNano());

        // 13. With milliseconds - slash format
        LocalDateTime dateTime13 = DateTimeUtils.parse("2023/12/25 15:30:45.123");
        assertEquals(2023, dateTime13.getYear());
        assertEquals(12, dateTime13.getMonthValue());
        assertEquals(25, dateTime13.getDayOfMonth());
        assertEquals(15, dateTime13.getHour());
        assertEquals(30, dateTime13.getMinute());
        assertEquals(45, dateTime13.getSecond());
        assertEquals(123000000, dateTime13.getNano());

        // 14. With milliseconds - dot format
        LocalDateTime dateTime14 = DateTimeUtils.parse("2023.12.25 15:30:45.123");
        assertEquals(2023, dateTime14.getYear());
        assertEquals(12, dateTime14.getMonthValue());
        assertEquals(25, dateTime14.getDayOfMonth());
        assertEquals(15, dateTime14.getHour());
        assertEquals(30, dateTime14.getMinute());
        assertEquals(45, dateTime14.getSecond());
        assertEquals(123000000, dateTime14.getNano());

        // 15. With microseconds
        LocalDateTime dateTime15 = DateTimeUtils.parse("2023-12-25 15:30:45.123456");
        assertEquals(2023, dateTime15.getYear());
        assertEquals(12, dateTime15.getMonthValue());
        assertEquals(25, dateTime15.getDayOfMonth());
        assertEquals(15, dateTime15.getHour());
        assertEquals(30, dateTime15.getMinute());
        assertEquals(45, dateTime15.getSecond());
        assertEquals(123456000, dateTime15.getNano());

        // 16. With nanoseconds
        LocalDateTime dateTime16 = DateTimeUtils.parse("2023-12-25 15:30:45.123456789");
        assertEquals(2023, dateTime16.getYear());
        assertEquals(12, dateTime16.getMonthValue());
        assertEquals(25, dateTime16.getDayOfMonth());
        assertEquals(15, dateTime16.getHour());
        assertEquals(30, dateTime16.getMinute());
        assertEquals(45, dateTime16.getSecond());
        assertEquals(123456789, dateTime16.getNano());

        // 17. Chinese format
        LocalDateTime dateTime17 = DateTimeUtils.parse("2023年12月25日 15时30分45秒");
        assertEquals(2023, dateTime17.getYear());
        assertEquals(12, dateTime17.getMonthValue());
        assertEquals(25, dateTime17.getDayOfMonth());
    }

    @Test
    public void testParseWithCustomFormat() {
        // Test parsing with custom format
        LocalDateTime dateTime1 =
                DateTimeUtils.parse("2023-12-25T15:30:45", "yyyy-MM-dd'T'HH:mm:ss");
        assertEquals(2023, dateTime1.getYear());
        assertEquals(12, dateTime1.getMonthValue());
        assertEquals(25, dateTime1.getDayOfMonth());
        assertEquals(15, dateTime1.getHour());
        assertEquals(30, dateTime1.getMinute());
        assertEquals(45, dateTime1.getSecond());

        LocalDateTime dateTime2 =
                DateTimeUtils.parse("2023年12月25日15时30分45秒", "yyyy年MM月dd日HH时mm分ss秒");
        assertEquals(2023, dateTime2.getYear());
        assertEquals(12, dateTime2.getMonthValue());
        assertEquals(25, dateTime2.getDayOfMonth());
    }

    @Test
    public void testParseWithFormatterEnum() {
        // Test parsing with Formatter enum
        LocalDateTime dateTime1 =
                DateTimeUtils.parse(
                        "2023-12-25 15:30:45", DateTimeUtils.Formatter.YYYY_MM_DD_HH_MM_SS);
        assertEquals(2023, dateTime1.getYear());
        assertEquals(12, dateTime1.getMonthValue());
        assertEquals(25, dateTime1.getDayOfMonth());
        assertEquals(15, dateTime1.getHour());
        assertEquals(30, dateTime1.getMinute());
        assertEquals(45, dateTime1.getSecond());
    }

    @Test
    public void testToString() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 15, 30, 45);

        // Test formatting with Formatter enum
        String formatted1 =
                DateTimeUtils.toString(dateTime, DateTimeUtils.Formatter.YYYY_MM_DD_HH_MM_SS);
        assertEquals("2023-12-25 15:30:45", formatted1);

        // Test formatting with custom format string
        String formatted2 = DateTimeUtils.toString(dateTime, "yyyy/MM/dd HH:mm:ss");
        assertEquals("2023/12/25 15:30:45", formatted2);
    }

    @Test
    public void testParseUnsupportedFormat() {
        // Test parsing with unsupported format
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    try {
                        DateTimeUtils.parse("2023/12/25T15:30:45");
                    } catch (Exception e) {
                        assertEquals(
                                "Unsupported datetime format: 2023/12/25T15:30:45", e.getMessage());
                        throw e;
                    }
                });
    }

    @Test
    public void testParsePerformanceAutoFormatPatternFirst() {
        // Test performance of auto-format parsing
        final int iterations = 10000000;
        String dateTimeStr = "2023-12-25 15:30:45";

        // Warm-up
        for (int i = 0; i < iterations / 1000; i++) {
            DateTimeUtils.parse(dateTimeStr);
        }

        // Measure performance
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DateTimeUtils.parse(dateTimeStr);
        }
        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        long durationMs = durationNs / 1_000_000;
        double durationUs = durationNs / 1000.0;
        System.out.printf(
                "Auto-format parsing performance: %d iterations in %d ms (%.3f μs/iteration)%n",
                iterations, durationMs, durationUs / iterations);

        // Ensure the operation is performed
        assertTrue(durationMs > 0);
    }

    @Test
    public void testParsePerformanceAutoFormatPatternFirstLast() {
        // Test performance of auto-format parsing
        final int iterations = 10000000;
        String dateTimeStr = "2023年12月25日 15时30分45秒";

        // Warm-up
        for (int i = 0; i < iterations / 1000; i++) {
            DateTimeUtils.parse(dateTimeStr);
        }

        // Measure performance
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DateTimeUtils.parse(dateTimeStr);
        }
        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        long durationMs = durationNs / 1_000_000;
        double durationUs = durationNs / 1000.0;
        System.out.printf(
                "Auto-format parsing performance: %d iterations in %d ms (%.3f μs/iteration)%n",
                iterations, durationMs, durationUs / iterations);

        // Ensure the operation is performed
        assertTrue(durationMs > 0);
    }

    @Test
    public void testParsePerformanceFormatterEnum() {
        // Test performance of parsing with Formatter enum
        final int iterations = 10000000;
        String dateTimeStr = "2023-12-25 15:30:45";
        DateTimeUtils.Formatter formatter = DateTimeUtils.Formatter.YYYY_MM_DD_HH_MM_SS;

        // Warm-up
        for (int i = 0; i < iterations / 1000; i++) {
            DateTimeUtils.parse(dateTimeStr, formatter);
        }

        // Measure performance
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DateTimeUtils.parse(dateTimeStr, formatter);
        }
        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        long durationMs = durationNs / 1_000_000;
        double durationUs = durationNs / 1000.0;
        System.out.printf(
                "Auto-format parsing performance: %d iterations in %d ms (%.3f μs/iteration)%n",
                iterations, durationMs, durationUs / iterations);

        // Ensure the operation is performed
        assertTrue(durationMs > 0);
    }

    @Test
    public void testParsePerformanceCustomFormat() {
        // Test performance of parsing with custom format string
        final int iterations = 10000000;
        String dateTimeStr = "2023-12-25 15:30:45";
        String formatStr = "yyyy-MM-dd HH:mm:ss";

        // Warm-up
        for (int i = 0; i < iterations / 1000; i++) {
            DateTimeUtils.parse(dateTimeStr, formatStr);
        }

        // Measure performance
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DateTimeUtils.parse(dateTimeStr, formatStr);
        }
        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        long durationMs = durationNs / 1_000_000;
        double durationUs = durationNs / 1000.0;
        System.out.printf(
                "Auto-format parsing performance: %d iterations in %d ms (%.3f μs/iteration)%n",
                iterations, durationMs, durationUs / iterations);

        // Ensure the operation is performed
        assertTrue(durationMs > 0);
    }

    @Test
    public void testToStringPerformance() {
        // Test performance of to string formatting
        final int iterations = 10000000;
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 15, 30, 45);
        DateTimeUtils.Formatter formatter = DateTimeUtils.Formatter.YYYY_MM_DD_HH_MM_SS;

        // Warm-up
        for (int i = 0; i < iterations / 1000; i++) {
            DateTimeUtils.toString(dateTime, formatter);
        }

        // Measure performance
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DateTimeUtils.toString(dateTime, formatter);
        }
        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        long durationMs = durationNs / 1_000_000;
        double durationUs = durationNs / 1000.0;
        System.out.printf(
                "Auto-format parsing performance: %d iterations in %d ms (%.3f μs/iteration)%n",
                iterations, durationMs, durationUs / iterations);

        // Ensure the operation is performed
        assertTrue(durationMs > 0);
    }
}
