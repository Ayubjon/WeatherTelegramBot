import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Parser{
    private final String url;

    public Parser(String city) {
        url = "https://sinoptik.ua/погода-" + city;
    }


    public String getTemperature() throws IOException {
        Document page = Jsoup.parse(new URL(url), 10000);
        Elements days = page.select("div.main");

        StringBuilder data = new StringBuilder();

        for(int i = 0; i < 5; ++i) {
            Element day = days.get(i);
            String date = day.select("p.date").first().text();
            String month = day.select("p.month").first().text();
            String min = day.select("div.min").first().select("span").first().text();
            String max = day.select("div.max").first().select("span").first().text();

            data.append(date).append(" ").append(month).append(": ").append(min).append(" - ").append(max).append("\n");
            //System.out.println(data);
        }

        return data.toString();
    }
}
