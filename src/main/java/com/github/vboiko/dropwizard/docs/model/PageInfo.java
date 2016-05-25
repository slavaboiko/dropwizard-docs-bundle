package com.github.vboiko.dropwizard.docs.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class PageInfo {

    private String fileName;
    private String permalink;
    private String layout;
    private Map<String, Object> dataFields;

    public PageInfo() {
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public ImmutableMap<String, Object> getDataFields() {
        return ImmutableMap.copyOf(dataFields);
    }

    public void setDataFields(Map<String, Object> dataFields) {
        this.dataFields = dataFields;
    }
}
