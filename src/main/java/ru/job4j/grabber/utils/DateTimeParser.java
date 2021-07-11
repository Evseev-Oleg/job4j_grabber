package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

/**
 * интерфейс описывающий парсинг даты
 */
public interface DateTimeParser {
    LocalDateTime parse(String parse);
}