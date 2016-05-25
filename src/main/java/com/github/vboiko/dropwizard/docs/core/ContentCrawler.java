package com.github.vboiko.dropwizard.docs.core;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;

import com.github.vboiko.dropwizard.docs.model.PageInfo;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.IOException;
import java.util.Set;

import io.dropwizard.Application;

public class ContentCrawler {

    private static final CharMatcher SLASHES = CharMatcher.is('/');

    private final String contentDir;
    private final ContentStore contentStore;
    private final ContentParser contentParser;

    public ContentCrawler(String contentDir, ContentStore contentStore, Class<? extends Application> appClass) {
        Preconditions.checkArgument(contentDir.startsWith("/"), "%s is not an absolute path", contentDir);
        Preconditions.checkArgument(!"/".equals(contentDir), "%s is the classpath root", contentDir);
        this.contentDir = SLASHES.trimLeadingFrom(contentDir);
        this.contentStore = contentStore;
        this.contentParser = new ContentParser(appClass);
    }

    public void crawl() throws IOException {
        Reflections reflections = new Reflections(contentDir, new ResourcesScanner());
        Set<String> resourceList = reflections.getResources(str -> true);

        Set<PageInfo> pageInfos = contentParser.parse(resourceList);

        pageInfos
            .stream()
            .forEach(pageInfo -> contentStore.put(pageInfo.getPermalink(), pageInfo));
    }
}
