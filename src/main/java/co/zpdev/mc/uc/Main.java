package co.zpdev.mc.uc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        for (int i = 48; i < 91; i++) {
            if (i > 57 && i < 65) continue;
            for (int j = 48; j < 91; j++) {
                if (j > 57 && j < 65) continue;
                for (int k = 48; k < 91; k++) {
                    if (k > 57 && k < 65) continue;
                    char[] chars = {(char) i, (char) j, (char) k};
                    run(new String(chars));
                    Thread.sleep(3000);
                }
            }
        }

    }

    private static void run(String username) throws IOException {
        String prefix = "https://namemc.com/name/";

        URL url = new URL(prefix + username);
        Document doc = Jsoup.parse(url, 10000);
        Elements elements = doc.select("#status-bar .col-sm-6");

        String status = elements.get(0).getElementsByTag("div").get(2).text();
        switch (status.toLowerCase()) {
            case "available":
                status = "\u001B[32m" + status;
                break;
            case "available later":
                status = "\u001B[93m" + status;
                break;
            case "blocked":
                status = "\u001B[33m" + status;
            case "unavailable":
            case "too short":
                status = "\u001B[31m" + status;
                break;
        }
        status += "\u001B[0m";

        System.out.println(username + " - " + status);
    }

}
