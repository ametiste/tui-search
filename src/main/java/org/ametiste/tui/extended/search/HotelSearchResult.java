package org.ametiste.tui.extended.search;

import lombok.Value;

import java.util.List;

@Value
public class HotelSearchResult {

    private Integer totalCount;
    private List<HotelInfo> results;
}
