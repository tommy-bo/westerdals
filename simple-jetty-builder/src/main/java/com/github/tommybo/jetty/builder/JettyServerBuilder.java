package com.github.tommybo.jetty.builder;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import com.github.tommybo.jetty.builder.extensions.JettyFilterBuilder;
import com.github.tommybo.jetty.builder.extensions.JettyListenerBuilder;
import com.github.tommybo.jetty.builder.extensions.JettyServletBuilder;
import com.github.tommybo.jetty.builder.extensions.ServletContextExtension;
import com.github.tommybo.jetty.builder.features.DefaultFeatures;
import com.github.tommybo.jetty.builder.features.JettyFeature;
import static org.eclipse.jetty.util.resource.Resource.*;

public class JettyServerBuilder {

    public static final String APPNAME = "appname";
    private static final String DEFAULT_CONTEXT_ROOT = "/";

    private final Integer port;
    private String contextRoot = DEFAULT_CONTEXT_ROOT;
    private int servletContextOptions = 0;
    private final List<ServletContextExtension> servletContextExtensions = new ArrayList<>();
    private final List<JettyFeature> jettyFeatures = new ArrayList<>();
    private final List<Resource> staticResources = new ArrayList<>();

    public static JettyServerBuilder usingPort(Integer port) {
        return new JettyServerBuilder(port);
    }

    private JettyServerBuilder(Integer port) {
        this.port = port;
        configureDefaultFeatures();
    }

    public JettyServerBuilder onContextRoot(String contextRoot) {
        this.contextRoot = ensureContextPathBeginsWithSlash(contextRoot);
        return this;
    }

    private String ensureContextPathBeginsWithSlash(String contextRoot) {
        return contextRoot.startsWith("/") ? contextRoot : "/" + contextRoot;
    }

    public JettyServerBuilder enableSessions() {
        servletContextOptions &= ServletContextHandler.SESSIONS;
        return this;
    }

    public JettyServletBuilder serve(String path) {
        JettyServletBuilder jettyServletBuilder = new JettyServletBuilder(this, path);
        servletContextExtensions.add(jettyServletBuilder);
        return jettyServletBuilder;
    }

    public JettyFilterBuilder filter(String path) {
        JettyFilterBuilder jettyFilterBuilder = new JettyFilterBuilder(this, path);
        servletContextExtensions.add(jettyFilterBuilder);
        return jettyFilterBuilder;
    }

    public JettyServerBuilder serveClassPathResources(String classPathResource) {
        staticResources.add(newClassPathResource(classPathResource));
        return this;
    }

    public JettyServerBuilder addListener(EventListener testListener) {
        servletContextExtensions.add(new JettyListenerBuilder(testListener));
        return this;
    }

    public Server create() {
        Server server = new Server(port);
        server.setHandler(opprettJettyHandlers(server));
        return enableFeaturesOn(server);
    }

    public Server start() throws Exception {
        Server server = create();
        server.start();
        return server;
    }

    public Server startAndWaitForShutdown() throws Exception {
        Server server = start();
        server.join();
        return server;
    }

    private HandlerCollection opprettJettyHandlers(Server server) {
        HandlerCollection rootHandlers = new HandlerCollection();
        rootHandlers.addHandler(opprettServletContextHandler(server));
        rootHandlers.addHandler(new DefaultHandler());
        return rootHandlers;
    }

    private ServletContextHandler opprettServletContextHandler(Server server) {
        ServletContextHandler contextHandler = new ServletContextHandler(server, contextRoot, servletContextOptions);
        for (ServletContextExtension jettyServletBuilder : servletContextExtensions) {
            jettyServletBuilder.addTo(contextHandler);
        }
        addStaticResources(contextHandler);
        return contextHandler;
    }

    private void addStaticResources(ServletContextHandler contextHandler) {
        if (!staticResources.isEmpty()) {
            contextHandler.addServlet(DefaultServlet.class, "/");
            Resource[] resourcesInReverseOrder = Iterables.toArray(Lists.reverse(staticResources), Resource.class);
            contextHandler.setBaseResource(new ResourceCollection(resourcesInReverseOrder));
        }
    }

    private void configureDefaultFeatures() {
        jettyFeatures.add(DefaultFeatures.ACCESS_LOGGING);
    }

    private Server enableFeaturesOn(Server server) {
        for (JettyFeature feature : jettyFeatures) {
            feature.enableFeatureOn(server);
        }
        return server;
    }
}
