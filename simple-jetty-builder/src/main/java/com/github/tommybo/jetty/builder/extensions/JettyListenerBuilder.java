package com.github.tommybo.jetty.builder.extensions;

import java.util.EventListener;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyListenerBuilder implements ServletContextExtension {
    private final EventListener listener;
    
    public JettyListenerBuilder(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void addTo(ServletContextHandler servletContextHandler) {
        servletContextHandler.addEventListener(listener);
    }

}
