package webserver.com.servlet.api;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import webserver.com.servlet.dto.UserJson;
import webserver.com.servlet.exceptions.ErrorHttpException;
import webserver.com.servlet.services.UserService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class UserApiServlet extends HttpServlet {

    public final static String GET_USER_PATH = "/api/user/*";
    private final UserService userService;
    private final Gson gson;

    public UserApiServlet(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userRawString = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        log.info("Received POST request to URI {} with body {}", request.getRequestURI(), userRawString);
        try {
            UserJson userDto = gson.fromJson(userRawString, UserJson.class);
            Long userId = userService.createUser(userDto);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader("Location", String.format("%s/%d", request.getRequestURI(), userId));
        } catch (Exception e) {
            log.error("exception occurred {}", e.getMessage());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            ErrorHttpException ex = new ErrorHttpException(e);
            servletOutputStream.print(gson.toJson(ex));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Received GET request to URI {}", request.getRequestURI());
        ServletOutputStream servletOutputStream = response.getOutputStream();
        try {
            URI uri = new URI(request.getRequestURI());
            Long userId = getUserIDFromUri(uri);
            if (userId < 0) {
                throw new ErrorHttpException("URI does not contain user id");
            }
            Optional<UserJson> user = userService.loadUser(userId);
            if (user.isEmpty()) {
                throw new ErrorHttpException(String.format("No such user with provided id %d", userId),
                        HttpServletResponse.SC_NOT_FOUND);

            }
            servletOutputStream.print(gson.toJson(user.get()));

        } catch (Exception e) {
            log.error("Exception occurred {}", e.toString());
            var ex = (e instanceof ErrorHttpException) ? (ErrorHttpException) e : new ErrorHttpException(e);
            servletOutputStream.print(gson.toJson(ex));
            response.setStatus(ex.getHttpStatusCode());
        }
    }

    private Long getUserIDFromUri(URI uri) {
        String[] fragments = uri.getPath().split("/");
        return fragments.length > 0 ? Long.valueOf(fragments[fragments.length - 1]) : Long.valueOf(-1);
    }
}
