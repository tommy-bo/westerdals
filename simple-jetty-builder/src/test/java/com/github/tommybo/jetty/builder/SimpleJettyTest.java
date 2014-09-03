package com.github.tommybo.jetty.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpleJettyTest {

    private static final String BASE_TEST_RESOURCES = "com/github/tommybo/jetty/builder/test/base";
    private static final String OVERRIDEN_TEST_RESOURCES = "com/github/tommybo/jetty/builder/test/overrides";
    private static final String DEFAULT_PORT = "8083";
    private final int port = findPort();
    private Server jettyServer;
    
    @After
    public void stopServer() throws Exception {
        if (jettyServer != null && jettyServer.isRunning()) {
            jettyServer.stop();
            jettyServer.join();
        }
    }

    @Test
    public void skalByggeEnJettyServerMedStatiskeFilerIgjennomDefaultServlet() throws Exception {
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .enableSessions()
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .start();

        verifyIndexAndCssFiles();
    }

    @Test
    public void skalByggeEnJettyServerMedEgenContextRoot() throws Exception {
        String testpath = "testpath";
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .onContextRoot(testpath)
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .start();

        verifyIndexAndCssFilesFromPath("/" + testpath);
    }
    
    @Test
    public void skalIkkeFeileDersomContextPathStarterMedSlash() throws Exception {
        String testpath = "/testpath";
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .onContextRoot(testpath)
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .start();

        verifyIndexAndCssFilesFromPath(testpath);
    }
    
    @Test
    public void skalByggeEnJettyServerMedFlereClassPathRessurser() throws Exception {
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .serveClassPathResources(OVERRIDEN_TEST_RESOURCES)
                .start();

        String css = readPageFromServer("/css/test.css");
        assertThat(css).startsWith(".test");
        assertThat(css).contains("font-weight: bold;");
        assertThat(css).contains("color: red;");
        String indexPage = readPageFromServer("/index.html");
        assertThat(indexPage).contains("tittel fra overstyrt ressurs");
        assertThat(indexPage).contains("innhold fra overstyrt ressurs");
    }
    
    @Test
    public void skalByggeEnJettyServerMedStatiskeFilerIgjennomDefaultServletOgFilter() throws Exception {
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .filter("/*").through(TestFilter.class)
                .start();

        assertThat(TestFilter.doFilterCalled).isFalse();
        
        verifyIndexAndCssFiles();

        assertThat(TestFilter.initCalled).isTrue();
        assertThat(TestFilter.doFilterCalled).isTrue();
        assertThat(TestFilter.destroyCalled).isFalse();

        jettyServer.stop();
        jettyServer.join();
        assertThat(TestFilter.destroyCalled).isTrue();
    }

    @Test
    public void skalByggeEnJettyServerMedFiltreringIgjennomEtEksisterendeFilter() throws Exception {
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .filter("/*").through(new TestFilter())
                .start();

        assertThat(TestFilter.doFilterCalled).isFalse();
        
        verifyIndexAndCssFiles();

        assertThat(TestFilter.initCalled).isTrue();
        assertThat(TestFilter.doFilterCalled).isTrue();
        assertThat(TestFilter.destroyCalled).isFalse();

        jettyServer.stop();
        jettyServer.join();
        assertThat(TestFilter.destroyCalled).isTrue();
    }

    @Test
    public void skalByggeEnJettyServerMedEgendefinertServlet() throws Exception {
        String servletPath = "/myServlet";
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .enableSessions()
                .serve(servletPath).with(TestServlet.class)
                .start();

        String indexPage = readPageFromServer(servletPath);
        assertThat(indexPage).contains("innhold fra TestServlet");
    }

    @Test
    public void skalByggeEnJettyServerMedEksisterendeServlet() throws Exception {
        String servletPath = "/myServlet";
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .enableSessions()
                .serve(servletPath).with(new TestServlet())
                .start();

        String indexPage = readPageFromServer(servletPath);
        assertThat(indexPage).contains("innhold fra TestServlet");
    }

    @Test
    public void skalAutomatiskGiAccessLogging() throws Exception {
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .serveClassPathResources(BASE_TEST_RESOURCES)
                .start();

        verifyIndexAndCssFiles();

        File logg = new File("target/logs/jetty-access.log");
        assertThat(logg).exists();
        assertThat(logg).isFile();
        assertThat(logg.length()).isGreaterThan(0);
    }

    @Test
    public void skalByggeEnJettyServerMedContextListener() throws Exception {
        TestListener testListener = new TestListener();
        jettyServer = JettyServerBuilder
                .usingPort(port)
                .addListener(testListener)
                .start();
        assertThat(testListener.hasBeenInitialized()).isTrue();
        assertThat(testListener.hasBeenDestroyed()).isFalse();
        
        jettyServer.stop();
        jettyServer.join();
        assertThat(testListener.hasBeenDestroyed()).isTrue();
    }

    @Test
    @Ignore("Kommer senere")
    public void skalByggeServerSomHosterGuice() throws Exception {
//        GuiceServletModule module;
//        JettyBuilder.usePort(8080).withGuiceModules(new PartsoekWebModule());
    }

    private void verifyIndexAndCssFiles() throws IOException {
        verifyIndexAndCssFilesFromPath("");
    }

    private void verifyIndexAndCssFilesFromPath(String path) throws IOException {
        String indexPage = readPageFromServer(path + "/index.html");
        assertThat(indexPage).contains("tittel fra baseressurs");
        assertThat(indexPage).contains("innhold fra baseressurs");

        String css = readPageFromServer(path + "/css/test.css");
        assertThat(css).startsWith(".test");
        assertThat(css).contains("font-weight: bold;");
        assertThat(css).contains("color: red;");
    }

    private String readPageFromServer(String page) throws IOException {
        String fullPath = "http://localhost:" + port + page;
        System.out.println("Connecting to " + fullPath);
        try (InputStream streamFromServer = new URL(fullPath).openConnection().getInputStream();
             InputStreamReader serverStreamReader = new InputStreamReader(streamFromServer, Charsets.UTF_8);) {
            return CharStreams.toString(serverStreamReader);
        }
    }

    private int findPort() {
        return Integer.parseInt(System.getProperty("jetty.test.port", DEFAULT_PORT));
    }

}
