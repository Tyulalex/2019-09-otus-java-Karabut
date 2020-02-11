package dependency.injection.controller;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@NoArgsConstructor
public class UserController {

    private static final String USER_LIST_URL = "/user/list";
    private static final String USER_LIST_HTML = "usersList.html";

    @GetMapping(value = {"/", USER_LIST_URL})
    public String getUserList() {
        return USER_LIST_HTML;
    }
}
