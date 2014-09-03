package com.github.tommybo.jetty.builder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(PrintWriter requestWriter = resp.getWriter()) {
            requestWriter.print("<html><head><meta charset=\"UTF-8\"></head><body>innhold fra TestServlet</body></html>");
        }
    }

}
