package com.github.tommybo.jetty.builder.features;

import org.eclipse.jetty.server.Server;

public interface JettyFeature {

    void enableFeatureOn(Server jettyServer);

}
