package com.github.vboiko.dropwizard.docs.model;

import com.google.common.collect.ImmutableMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class PageInfoBuilder {

    private PageInfo pageInfo = new PageInfo();
    private Map<String, Object> fields = new HashMap<>();

    public static PageInfoBuilder pageInfo() {
        return new PageInfoBuilder();
    }

    public PageInfoBuilder withData(Map<String, Object> fields) {
        this.fields.putAll(fields);
        return this;
    }

    public PageInfoBuilder withContent(String content) {
        this.fields.put("content", content);
        return this;
    }

    public PageInfoBuilder withFileName(String fileName) {
        pageInfo.setFileName(fileName);
        pageInfo.setPermalink(FilenameUtils.getBaseName(fileName));
        return this;
    }

    public PageInfo build() {

        try {
            BeanUtils.populate(pageInfo, fields);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        pageInfo.setDataFields(ImmutableMap.copyOf(fields));

        return pageInfo;
    }
}
