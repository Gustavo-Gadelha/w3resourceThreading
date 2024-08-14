package Exercise6;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int TOTAL_THREADS = 8;
    private static final int MAX_DEPTH = 2;
    private static final HashSet<String> VISITED_URLS = new HashSet<>();

    public static void main(String[] args) {
        String[] urls = new String[]{
                "https://pt.wikipedia.org/wiki/J%C3%BApiter",
                "https://pt.wikipedia.org/wiki/Saturno",
                "https://pt.wikipedia.org/wiki/Urano",
                "https://pt.wikipedia.org/wiki/Marte",
                "https://pt.wikipedia.org/wiki/Terra_(desambigua%C3%A7%C3%A3o)"
        };

        Instant start = Instant.now();
        try (ExecutorService executor = Executors.newFixedThreadPool(TOTAL_THREADS)) {
            for (String url : urls) {
                executor.submit(() -> crawl(url, 0));
            }
        }
        Instant end = Instant.now();

        System.out.printf("Crawler finished in %dms", Duration.between(start, end).toMillis());
    }

    private static void crawl(String url, int depth) {
        if (depth > MAX_DEPTH || VISITED_URLS.contains(url)) {
            return;
        }

        VISITED_URLS.add(url);
        System.out.printf("DEPTH: %d - %s Crawling through %s\n", depth, Thread.currentThread().getName(),  url);

        try {
            Document document = Jsoup.connect(url).get();
            System.out.printf("Accessing document \"%s\"\n", document.title());

            Elements links = document.select("a[href]");
            links.forEach((link) -> {
                if (isValid(link)) crawl(link.absUrl("href"), depth + 1);
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isValid(Element link) {
        return (link.absUrl("href").startsWith("https://pt"));
    }
}