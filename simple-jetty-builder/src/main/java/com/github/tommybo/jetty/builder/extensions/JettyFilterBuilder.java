package com.github.tommybo.jetty.builder.extensions;

import javax.servlet.Filter;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import com.github.tommybo.jetty.builder.JettyServerBuilder;

public class JettyFilterBuilder implements ServletContextExtension {
    private final JettyServerBuilder serverBuilder;
    private final String path;
    private FilterHolder filterHolder;

    public JettyFilterBuilder(JettyServerBuilder serverBuilder, String path) {
        this.serverBuilder = serverBuilder;
        this.path = path;
    }

    public JettyServerBuilder through(Class<? extends Filter> filterClass) {
        this.filterHolder = new FilterHolder(filterClass);
        return serverBuilder;
    }

    public JettyServerBuilder through(Filter filter) {
        this.filterHolder = new FilterHolder(filter);
        return serverBuilder;
    }

    @Override
    public void addTo(ServletContextHandler servletContextHandler) {
        servletContextHandler.addFilter(filterHolder, path, null);
    }
}
