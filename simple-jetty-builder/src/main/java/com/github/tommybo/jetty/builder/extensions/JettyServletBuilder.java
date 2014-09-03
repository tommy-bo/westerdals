package com.github.tommybo.jetty.builder.extensions;

import javax.servlet.Servlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.github.tommybo.jetty.builder.JettyServerBuilder;

public class JettyServletBuilder implements ServletContextExtension {
    private final JettyServerBuilder serverBuilder;
    private final String path;
    private ServletHolder servletHolder;

    public JettyServletBuilder(JettyServerBuilder serverBuilder, String path) {
        this.serverBuilder = serverBuilder;
        this.path = path;
    }

    public JettyServerBuilder with(Class<? extends Servlet> servletClass) {
        this.servletHolder = new ServletHolder(servletClass);
        return serverBuilder;
    }

    public JettyServerBuilder with(Servlet servlet) {
        this.servletHolder = new ServletHolder(servlet);
        return serverBuilder;
    }

    @Override
    public void addTo(ServletContextHandler contextHandler) {
        contextHandler.addServlet(servletHolder, path);
    }
}
