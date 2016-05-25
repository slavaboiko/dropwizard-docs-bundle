package com.github.vboiko.dropwizard.docs.core;

import com.github.vboiko.dropwizard.docs.resources.DocsResource;

import java.io.IOException;
import java.util.Collections;

import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;

public class DocsBundle implements Bundle {

    private final ContentStore contentStore;
    private final ContentCrawler crawler;
    private final String viewsDir;

    public DocsBundle(String contentDir, String viewsDir, Class<? extends Application> appClass) {
        this.viewsDir = viewsDir;
        this.contentStore = new ContentStore();
        this.crawler = new ContentCrawler(contentDir, contentStore, appClass);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>(Collections.singletonList(new FreemarkerViewRenderer())));
        bootstrap.addBundle(new AssetsBundle("/assets", "/__assets", null, null));
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new DocsResource(viewsDir, contentStore));

        try {
            crawler.crawl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
