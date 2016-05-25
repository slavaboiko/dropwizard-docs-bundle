package com.github.vboiko.dropwizard.docs.core;

import com.github.vboiko.dropwizard.docs.model.PageInfo;

import java.util.HashMap;
import java.util.Map;

public class ContentStore {

    private final Map<String, PageInfo> pages = new HashMap<>();

    public void put(String permalink, PageInfo page) {
        pages.put(permalink, page);
    }

    public PageInfo getPage(String permalink) {
        return pages.get(permalink);
    }
}
