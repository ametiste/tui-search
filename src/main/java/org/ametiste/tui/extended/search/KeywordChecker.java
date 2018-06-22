package org.ametiste.tui.extended.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeywordChecker {

    public boolean check(String uri, List<String> keywords) throws IOException {

        Document doc = Jsoup.connect(uri).get();
        List<String> features = doc.select("#informatie-en-kaart ul li").stream().map(element -> element.text()).collect(Collectors.toList());
        for(String keyword: keywords) {
            if(features.contains(keyword)) {
                return true;
            }
        }

        //todo tegen betaling
        return false;
    }
}
