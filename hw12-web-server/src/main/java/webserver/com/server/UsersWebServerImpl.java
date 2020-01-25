package webserver.com.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import webserver.com.helpers.FileSystemHelper;
import webserver.com.servlet.AuthorizationFilter;
import webserver.com.servlet.api.UserApiServlet;
import webserver.com.servlet.api.UsersApiServlet;
import webserver.com.servlet.services.TemplateProcessor;
import webserver.com.servlet.services.UserAuthService;
import webserver.com.servlet.services.UserService;
import webserver.com.servlet.webpages.LoginServlet;
import webserver.com.servlet.webpages.UsersListServlet;

import java.util.stream.IntStream;

public class UsersWebServerImpl implements UsersWebServer {

    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String START_PAGE_NAME = "index.html";
    private final int port;
    private final UserService userService;
    private final Server server;
    private final Gson gson;
    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthServiceForFilterBasedSecurity;

    public UsersWebServerImpl(int port, UserService userService, Gson gson,
                              TemplateProcessor templateProcessor, UserAuthService userAuthServiceForFilterBasedSecurity) {
        this.port = port;
        this.userService = userService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.userAuthServiceForFilterBasedSecurity = userAuthServiceForFilterBasedSecurity;
        this.server = initContext();
    }

    @Override
    public void start() throws Exception {
        this.server.start();

    }

    @Override
    public void join() throws Exception {
        this.server.join();

    }

    @Override
    public void stop() throws Exception {
        this.server.stop();
    }

    private Server initContext() {
        HandlerList handlers = new HandlerList();
        ResourceHandler resourceHandler = createResourceHandler();
        handlers.addHandler(resourceHandler);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(
                new ServletHolder(new UsersApiServlet(userService, gson)), UsersApiServlet.GET_USERS_PATH);
        servletContextHandler.addServlet(
                new ServletHolder(new UserApiServlet(userService, gson)), UserApiServlet.GET_USER_PATH);

        servletContextHandler.addServlet(
                new ServletHolder(new UsersListServlet(templateProcessor)), UsersListServlet.PATH);
        handlers.addHandler(servletContextHandler);
        handlers.addHandler(applyFilterBasedSecurity(servletContextHandler,
                UsersApiServlet.GET_USERS_PATH,
                UserApiServlet.GET_USER_PATH,
                UsersListServlet.PATH
        ));
        Server server = new Server(port);
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler applyFilterBasedSecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor,
                userAuthServiceForFilterBasedSecurity)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        IntStream.range(0, paths.length)
                .forEachOrdered(i -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter),
                        paths[i], null));
        return servletContextHandler;
    }
}
