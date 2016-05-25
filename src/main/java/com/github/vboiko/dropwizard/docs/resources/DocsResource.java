package com.github.vboiko.dropwizard.docs.resources;

import com.github.vboiko.dropwizard.docs.core.ContentStore;
import com.github.vboiko.dropwizard.docs.model.PageInfo;
import com.github.vboiko.dropwizard.docs.views.PageView;
import com.github.vboiko.dropwizard.docs.views.PageViewBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/docs")
public class DocsResource {

    private final String viewsDir;
    private final ContentStore contentStore;

    public DocsResource(String viewsDir, ContentStore contentStore) {
        this.viewsDir = viewsDir;
        this.contentStore = contentStore;
    }

    @GET
    @Path("/")
    public PageView getIndex() {
        return getPage("index");
    }

    @GET
    @Path("/{page:[a-zA-Z0-9-_]+}")
    public PageView getPage(@PathParam("page") String page) {
        PageInfo pageInfo = contentStore.getPage(page);

        if (pageInfo == null) {
            throw new NotFoundException("Page not found: " + page);
        }

        return PageViewBuilder.view()
                              .fromDir(viewsDir)
                              .info(pageInfo)
                              .build();
    }
}
