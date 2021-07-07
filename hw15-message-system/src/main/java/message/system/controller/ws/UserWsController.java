package message.system.controller.ws;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.system.dto.MessageDto;
import message.system.dto.UserDto;
import message.system.frontendservice.FrontendService;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserWsController {

    private final Gson gson;
    private final PasswordEncoder passwordEncoder;
    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/users")
    @SendToUser("/queue/reply")
    public String processMessageFromClient(
            @Payload String message,
            Principal principal) throws Exception {
        MessageDto msg = toMessage(message);
        log.info("incoming message {}", msg);
        switch (msg.getMessageType()) {
            case CREATE_USER:
                frontendService.createUser(toUserDto(msg), data -> log.info("User is created"));
                getUsersList(principal.getName());
            case GET_LIST_OF_USERS:
                getUsersList(principal.getName());
            default:
                return "Unsupported operation";
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    private void getUsersList(String username) {
        frontendService.getUsersData(
                data -> simpMessagingTemplate.convertAndSendToUser(username, "/queue/reply", data)
        );
    }

    private MessageDto toMessage(String message) {
        return gson.fromJson(message, MessageDto.class);
    }

    private UserDto toUserDto(MessageDto messageDto) {
        UserDto userDto = gson.fromJson(messageDto.getPayload(), UserDto.class);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userDto;
    }
}
