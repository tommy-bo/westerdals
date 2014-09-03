package com.github.tommybo.jetty.builder.features;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import ch.qos.logback.access.jetty.RequestLogImpl;
import com.github.tommybo.jetty.builder.JettySetupException;

class AccessLoggingFeature implements JettyFeature {
    
    @Override
    public void enableFeatureOn(Server jettyServer) {
        HandlerCollection jettyHandlers = hentHandlerCollection(jettyServer);
        jettyHandlers.addHandler(getRequestLogHandler(jettyServer));
        jettyServer.setHandler(jettyHandlers);
    }

    private HandlerCollection hentHandlerCollection(Server jettyServer) throws RuntimeException {
        Handler handler = jettyServer.getHandler();
        if(!(handler instanceof HandlerCollection)) {
            throw new JettySetupException("Serveren har ikke stÃ¸tte for access-logging. Serveren mÃ¥ ha en HandlerCollection som handler");
        }
        HandlerCollection handlerList = (HandlerCollection) handler;
        return handlerList;
    }
    
    private Handler getRequestLogHandler(Server jettyServer) {
        RequestLogImpl requestLog = new RequestLogImpl();
        requestLog.setQuiet(true);
        requestLog.setResource("/logback-access.xml");
        RequestLogHandler logHandler = new RequestLogHandler();
        logHandler.setRequestLog(requestLog);
        return logHandler;
    }

    private String castServerProperty(Object attribute) {
        if (attribute instanceof String) {
            return attribute.toString();
        } else {
            return null;
        }
    }
}
