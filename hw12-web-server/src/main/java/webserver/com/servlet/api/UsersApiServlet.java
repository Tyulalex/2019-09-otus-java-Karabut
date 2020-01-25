package webserver.com.servlet.api;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import webserver.com.servlet.dto.UserJson;
import webserver.com.servlet.services.UserService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class UsersApiServlet extends HttpServlet {

    public final static String GET_USERS_PATH = "/api/users";

    private final UserService userService;
    private final Gson gson;

    public UsersApiServlet(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UserJson> users = userService.getUsers();
        ServletOutputStream outputStream = response.getOutputStream();

        outputStream.print(gson.toJson(users));
    }
}
