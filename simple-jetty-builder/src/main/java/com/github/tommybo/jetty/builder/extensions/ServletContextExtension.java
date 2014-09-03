package com.github.tommybo.jetty.builder.extensions;

import org.eclipse.jetty.servlet.ServletContextHandler;

public interface ServletContextExtension {
    void addTo(ServletContextHandler servletContextHandler);
}
