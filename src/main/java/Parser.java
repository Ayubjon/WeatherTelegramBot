import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class Parser{
    String url;

    public Parser(String city) {
        url = "https://sinoptik.ua/погода-" + city;
    }


    public String[] getTemperature() {
        Document page;
        Elements days;
        try{
            page = Jsoup.parse(new URL(url), 10000);
            days = page.select("div.main");
        } catch (Exception e) {
            return new String[1];
        }

        String[] data = new String[5];

        for(int i = 0; i < 5; ++i) {
            Element day = days.get(i);
            String date = day.select("p.date").first().text();
            String month = day.select("p.month").first().text();
            String min = day.select("div.min").first().select("span").first().text();
            String max = day.select("div.max").first().select("span").first().text();

            data[i] = date + " " + month + ": " + min + " - " + max;
            System.out.println(data[i]);
        }

        return data;
    }
}
