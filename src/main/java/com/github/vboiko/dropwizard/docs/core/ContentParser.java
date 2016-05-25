package com.github.vboiko.dropwizard.docs.core;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.github.vboiko.dropwizard.docs.model.PageInfo;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.pegdown.PegDownProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.dropwizard.Application;

import static com.github.vboiko.dropwizard.docs.model.PageInfoBuilder.pageInfo;

public class ContentParser {

    private final Class<? extends Application> appClass;
    private final PegDownProcessor pegDownProcessor;
    private final Yaml yaml;

    public ContentParser(Class<? extends Application> appClass) {
        this.appClass = appClass;
        this.pegDownProcessor = new PegDownProcessor();
        this.yaml = new Yaml();
    }

    public Set<PageInfo> parse(Set<String> resources) throws IOException {

        return resources
            .stream()
            .map(resource -> {
                String fileName = FilenameUtils.getName(resource);
                String resourcePath = "/" + resource;
                URL resourceUrl = Resources.getResource(appClass, resourcePath);

                Preconditions.checkArgument(resourceUrl != null, "File %s does not exist", resourcePath);

                try (InputStream inputStream = resourceUrl.openStream()) {
                    return parse(fileName, new InputStreamReader(inputStream));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toSet());
    }

    private PageInfo parse(String fileName, Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);

        // detect YAML front matter
        String line = br.readLine();
        while (line.isEmpty()) {
            line = br.readLine();
        }
        if (!line.matches("[-]{3,}")) { // use at least three dashes
            throw new IllegalArgumentException("No YAML Front Matter");
        }

        final String delimiter = line;

        // scan YAML front matter
        StringBuilder sb = new StringBuilder();
        line = br.readLine();
        while (!line.equals(delimiter)) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }

        // parse data
        Map<String, Object> fields = parseYamlFrontMatter(sb.toString());
        String content = parseMarkdown(br);

        if (!fields.containsKey("layout")) {
            throw new IllegalArgumentException("Layout field is mandatory. (missing in " + fileName + ")");
        }

        return pageInfo()
            .withFileName(fileName)
            .withContent(content)
            .withData(fields)
            .build();
    }

    private String parseMarkdown(BufferedReader br) throws IOException {
        return pegDownProcessor.markdownToHtml(IOUtils.toString(br));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseYamlFrontMatter(String content) {
        return (Map<String, Object>) yaml.load(content);
    }
}
