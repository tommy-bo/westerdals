package com.github.tommybo.jetty.builder;

public class JettySetupException extends RuntimeException {

    public JettySetupException(String message) {
        super(message);
    }

    public JettySetupException(String message, Throwable cause) {
        super(message, cause);
    }
}
