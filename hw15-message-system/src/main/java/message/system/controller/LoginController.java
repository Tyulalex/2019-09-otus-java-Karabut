package message.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final String LOGIN_URL = "/login";

    @GetMapping(LOGIN_URL)
    public String login(Model model) {
        return "login";
    }
}
