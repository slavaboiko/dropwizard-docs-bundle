package com.github.vboiko.dropwizard.docs.views;

import com.github.vboiko.dropwizard.docs.model.PageInfo;

import org.apache.commons.io.FilenameUtils;

public class PageViewBuilder {

    private String viewsDir;
    private PageInfo pageInfo;

    public static PageViewBuilder view() {
        return new PageViewBuilder();
    }

    public PageViewBuilder fromDir(String viewsDir) {
        this.viewsDir = viewsDir;
        return this;
    }

    public PageViewBuilder info(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public PageView build() {
        String url = FilenameUtils.concat(viewsDir, pageInfo.getLayout());
        return new PageView(url, pageInfo.getDataFields());
    }
}
