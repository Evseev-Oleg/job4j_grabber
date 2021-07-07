package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс показывает как распарсить HTML страницу используя JSOUP.
 */

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers/";
        for (int i = 1; i <= 5; i++) {
            System.out.println("Страница: ======================================== " + i);
            parseHtml(url + i);
        }
    }

    /**
     * Метод парсит HTML страницу.
     *
     * @param url ссылка на страницу
     * @throws IOException .
     */
    private static void parseHtml(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href1 = td.child(0);
            System.out.println(href1.attr("href"));
            System.out.println(href1.text());
            Element data = td.parent().child(5);
            System.out.println(data.text());
        }
    }


    /**
     * метод парсит html страницу и записывает
     * результат в List<Post>
     *
     * @param url ссылка на страницу
     * @return возвращает лист сущностей Post
     * @throws IOException .
     */
    public List<Post> createPost(String url) throws IOException {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        List<Post> postList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            Document doc1 = Jsoup.connect(href.attr("href")).get();
            Elements vacancy = doc1.select(".messageHeader");
            Elements textVacancy = doc1.select(".msgBody");
            Element data = td.parent().child(5);
            postList.add(new Post(vacancy.get(0).text(), textVacancy.get(1).text(),
                    url, sqlRuDateTimeParser.parse(data.text())));
        }
        return postList;
    }
}
