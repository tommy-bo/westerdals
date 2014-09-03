package com.github.tommybo.jetty.builder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class TestFilter implements Filter {

    public static boolean initCalled = false;
    public static boolean doFilterCalled = false;
    public static boolean destroyCalled = false;

    public TestFilter() {
        initCalled = false;
        doFilterCalled = false;
        destroyCalled = false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initCalled = true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilterCalled = true;
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        destroyCalled = true;
    }

}
