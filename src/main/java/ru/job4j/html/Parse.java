package ru.job4j.html;

import ru.job4j.Post;

import java.io.IOException;
import java.util.List;

/**
 * интерфейс описывающий парсинг сайта
 */
public interface Parse {
    List<Post> list(String link) throws IOException;

    Post detail(String link) throws IOException;
}
