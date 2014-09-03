package com.github.tommybo.jetty.builder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

class TestListener implements ServletContextListener {
    private boolean initialized;
    private boolean destroyed;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initialized = true;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        destroyed = true;
    }

    public boolean hasBeenInitialized() {
        return initialized;
    }

    public boolean hasBeenDestroyed() {
        return destroyed;
    }

}
