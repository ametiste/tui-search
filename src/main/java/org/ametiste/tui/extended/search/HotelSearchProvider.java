package org.ametiste.tui.extended.search;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class HotelSearchProvider {

    @Autowired
    private KeywordChecker checker;

    public List<HotelInfo> hotelsPerPage(String baseUrl, int page, List<String> keywords) throws IOException {

        log.info("Searching " + keywords + " on page " + page);

        String uri = baseUrl + "?page=" + page;

        Document doc = Jsoup.connect(uri).get();
        List<HotelInfo> hotels = new ArrayList<>();

        Elements newsHeadlines = doc.select("#SearchResult .sr-item-hdr a.acconame");
        for (Element headline : newsHeadlines) {
            String hotelUri = headline.absUrl("href");

            if(checker.check(hotelUri, keywords)) {
                hotels.add(new HotelInfo(headline.text(),
                        hotelUri,
                        Integer.parseInt(headline.parent().parent().parent().select("a.price span").get(0).text())));
            }
        }
        return hotels;

    }
}
