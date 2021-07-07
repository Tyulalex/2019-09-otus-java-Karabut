package message.system.repository.handlers;

import lombok.AllArgsConstructor;
import message.system.dto.UserDto;
import message.system.messagesystem.Message;
import message.system.messagesystem.MessageType;
import message.system.messagesystem.RequestHandler;
import message.system.service.UserService;
import message.system.utils.Serializers;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GetUsersRequestHandler implements RequestHandler {

    private final UserService userService;

    @Override
    public Optional<Message> handle(Message msg) {
        List<UserDto> users = userService.getUsers();

        return Optional.of(
                Message.builder()
                        .from(msg.getTo())
                        .to(msg.getFrom())
                        .payload(Serializers.serialize(users))
                        .sourceMessageId(msg.getId())
                        .type(MessageType.GET_USERS_DATA.getValue()).build()
        );
    }
}
