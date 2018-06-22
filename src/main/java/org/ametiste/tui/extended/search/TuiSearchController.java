package org.ametiste.tui.extended.search;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class TuiSearchController {

    private String baseUri = "https://www.tui.nl/vakantie";
    private String postfix = "/resultaten/";
    private List<String> defaultKeywordSearch = Collections.singletonList("Oppasservice beschikbaar");

    @Autowired
    private HotelSearchProvider searcher;

    @RequestMapping("/")
    public HotelSearchResult search(@RequestParam Optional<String> country,
                                    @RequestParam(name = "keyword") Optional<List<String>> paramKeywords,
                                    @RequestParam Optional<Integer> from, @RequestParam Optional<Integer> to
    ) throws IOException {

        String uri = build(country);

        List<String> keywords = paramKeywords.orElse(defaultKeywordSearch);
        Document doc = Jsoup.connect(uri).get();
        String text = doc.select("#SearchResult .paging li.last").get(0).text();
        int lastExistingPage = Integer.parseInt(text);

        Integer firstPage = from.orElse(1);
        Integer lastPage = to.orElse(lastExistingPage);

        Integer toPage = lastPage - firstPage < 5 ? lastPage : firstPage + 5;

        List<HotelInfo> combinedResult = new ArrayList<>();
        for (int i = firstPage; i <= toPage; i++) {
            combinedResult.addAll(searcher.hotelsPerPage(uri, i, keywords));
        }

        return new HotelSearchResult(combinedResult.size(), combinedResult);
    }

    private String build(Optional<String> country) {

        String pathPart = country.map(c -> "/" + c).orElse("");
        return baseUri + pathPart + postfix;
    }
}
