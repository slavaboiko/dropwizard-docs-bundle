package com.github.vboiko.dropwizard.docs.views;

import com.google.common.collect.ImmutableMap;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import io.dropwizard.views.View;

public class PageView extends View implements TemplateHashModel {

    private final DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
    private final ImmutableMap<String, Object> data;

    public PageView(String url, ImmutableMap<String, Object> data) {
        super(url);
        this.data = data;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        if (!data.containsKey(key)) {
            return null;
        }
        return wrapper.wrap(data.get(key));
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return data.isEmpty();
    }
}
