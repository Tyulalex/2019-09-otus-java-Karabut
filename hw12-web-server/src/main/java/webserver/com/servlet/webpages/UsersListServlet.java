package webserver.com.servlet.webpages;

import webserver.com.servlet.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class UsersListServlet extends HttpServlet {

    public static final String PATH = "/users";
    private static final String USERS_LIST_PAGE_TEMPLATE = "usersList.html";
    private final TemplateProcessor templateProcessor;


    public UsersListServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_LIST_PAGE_TEMPLATE, Collections.emptyMap()));
    }
}
