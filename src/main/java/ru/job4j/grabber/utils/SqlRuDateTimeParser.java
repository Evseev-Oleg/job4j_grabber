package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        String[] parseData = parse.split(" ");
        if (parseData.length == 4) {
            Map<String, Month> parseMonth = new HashMap<>();
            parseMonth.put("янв", Month.JANUARY);
            parseMonth.put("фев", Month.FEBRUARY);
            parseMonth.put("мар", Month.MARCH);
            parseMonth.put("апр", Month.APRIL);
            parseMonth.put("май", Month.MAY);
            parseMonth.put("июн", Month.JUNE);
            parseMonth.put("июл", Month.JULY);
            parseMonth.put("авг", Month.AUGUST);
            parseMonth.put("сен", Month.SEPTEMBER);
            parseMonth.put("окт", Month.OCTOBER);
            parseMonth.put("ноя", Month.NOVEMBER);
            parseMonth.put("дек", Month.DECEMBER);
            String yearFar = "20" + parseData[2].replace(
                    ",", "");
            Month monthFar = parseMonth.get(parseData[1]);
            String dayFar = parseData[0];
            String[] parseTimeFar = parseData[3].split(":");
            int hourFar = Integer.parseInt(parseTimeFar[0]);
            int minFar = Integer.parseInt(parseTimeFar[1]);
            return LocalDateTime.of(Integer.parseInt(yearFar),
                    monthFar, Integer.parseInt(dayFar), hourFar, minFar, 0);
        } else {
            LocalDateTime today = LocalDateTime.now();
            String[] parseTimeNear = parseData[1].split(":");
            if (parseData[0].contains("вчера")) {
                LocalDateTime yesterday = today.minusDays(1);
                int yearYesterday = yesterday.getYear();
                Month monthYesterday = today.getMonth();
                int dayYesterday = yesterday.getDayOfMonth();
                return LocalDateTime.of(yearYesterday, monthYesterday,
                        dayYesterday, Integer.parseInt(parseTimeNear[0]),
                        Integer.parseInt(parseTimeNear[1]), 0);
            }
            if (parseData[0].contains("сегодня")) {
                return LocalDateTime.of(today.getYear(), today.getMonth(),
                        today.getDayOfMonth(), Integer.parseInt(parseTimeNear[0]),
                        Integer.parseInt(parseTimeNear[1]), 0);
            }
            return null;
        }
    }
}
