package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс показывает как распарсить HTML страницу используя JSOUP.
 */
public class SqlRuParse implements Parse {
    private DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers/";
        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> postList = sqlRuParse.list(url);
        for (Post pos : postList) {
            System.out.println(pos.getCreated());
        }

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
     * Метод загружает список всех Post
     *
     * @param link принимает строку - ссылку сайта
     * @return возвращает лист сущностей
     * @throws IOException .
     */
    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> listPost = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            Post post = detail(href.attr("href"));
            if (post.getTitle().contains("Java")) {
                listPost.add(post);
            }
        }
        return listPost;
    }

    /**
     * Метод загружает детали одного Post
     *
     * @param link принимает ссылку на пост
     * @return возвращает сущность Post
     * @throws IOException .
     */
    @Override
    public Post detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        String data = doc.select("td[class=msgFooter]").get(0).text();
        Elements vacancy = doc.select(".messageHeader");
        Elements textVacancy = doc.select(".msgBody");
        return new Post(vacancy.get(0).text(), textVacancy.get(1).text(),
                link, dateTimeParser.parse(data));
    }
}
