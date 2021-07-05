package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, Month> MONTH_MAP =
            Map.ofEntries(Map.entry("янв", Month.JANUARY),
                    Map.entry("фев", Month.FEBRUARY),
                    Map.entry("мар", Month.MARCH),
                    Map.entry("апр", Month.APRIL),
                    Map.entry("май", Month.MAY),
                    Map.entry("июн", Month.JUNE),
                    Map.entry("июл", Month.JULY),
                    Map.entry("авг", Month.AUGUST),
                    Map.entry("сен", Month.SEPTEMBER),
                    Map.entry("окт", Month.OCTOBER),
                    Map.entry("ноя", Month.NOVEMBER),
                    Map.entry("дек", Month.DECEMBER));

    @Override
    public LocalDateTime parse(String parse) {
        String[] parseData = parse.split(" ");
        if (parseData.length == 4) {
            String yearFar = "20" + parseData[2].replace(
                    ",", "");
            Month monthFar = MONTH_MAP.get(parseData[1]);
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
