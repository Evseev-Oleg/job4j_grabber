package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Класс показывает как распарсить HTML страницу используя JSOUP.
 */

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers/1";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.select(".sort_options");
        Elements listPages = table.select("td[style=text-align:left]");
        Elements links = listPages.select("a");
        for (Element link : links) {
            parseHtml(url);
            url = link.attr("href");
            System.out.println("Следующая страница: ======================================== " + link.text());
            if (Integer.parseInt(link.text()) > 5) {
                break;
            }
        }
    }

    /**
     * Метод парсит HTML страницу.
     *
     * @param url ссылка на страницу
     * @throws IOException
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
}
