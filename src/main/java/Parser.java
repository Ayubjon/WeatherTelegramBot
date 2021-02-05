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


    public String[] getTemperature() throws Exception {
        Document page = Jsoup.parse(new URL(url), 10000);
        Elements days = page.select("div.main");

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

    public static void main(String[] args) {
        Parser par = new Parser("душанбе");
        try{
            par.getTemperature();
        } catch(Exception e) {
            System.out.println(e + "error");
        }
    }
}
