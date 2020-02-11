package dependency.injection.controller;

import dependency.injection.dto.UserDto;
import dependency.injection.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class UserRestController {

    private static final String CREATE_USER_URL = "/api/user";
    private static final String GET_USER_URL = "/api/user/{userId}";
    private static final String GET_USERS = "/api/users";

    private final UserService userService;
    private ServletContext servletContext;

    @GetMapping(value = GET_USER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") long userId) {
        Optional<UserDto> user = userService.loadUser(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user.get());
        }
    }

    @GetMapping(value = GET_USERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = CREATE_USER_URL, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        Long userId = userService.createUser(userDto);
        String entityUri = String.format("%s/api/user/%d", servletContext.getContextPath(), userId);
        return ResponseEntity.created(URI.create(entityUri)).build();
    }
}
