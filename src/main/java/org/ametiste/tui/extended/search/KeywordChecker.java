package org.ametiste.tui.extended.search;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeywordChecker {

    public boolean check(String uri, List<String> keywords) {

        Document doc;
        try {
            doc = Jsoup.connect(uri).get();
        } catch (IOException e) {
            log.error("Exception caught calling uri " + uri, e);
            return false;
        }
        List<String> features = doc.select("#informatie-en-kaart ul li").stream().map(Element::text).collect(Collectors.toList());
        for (String keyword : keywords) {
            if (features.contains(keyword)) {
                return true;
            }
        }

        //todo tegen betaling
        return false;
    }
}
