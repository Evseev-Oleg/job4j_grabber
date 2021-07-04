package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href1 = td.child(0);
            System.out.println(href1.attr("href"));
            System.out.println(href1.text());
            Element data = td.parent().child(5);
            System.out.println(data.text());
            if (Integer.parseInt(td.text()) > 5) {
                break;
            }
        }
    }
}