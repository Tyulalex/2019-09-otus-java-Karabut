package message.system.repository.handlers;

import lombok.AllArgsConstructor;
import message.system.dto.UserDto;
import message.system.messagesystem.Message;
import message.system.messagesystem.MessageType;
import message.system.messagesystem.RequestHandler;
import message.system.service.UserService;
import message.system.utils.Serializers;

import java.util.Optional;

@AllArgsConstructor
public class CreateUserRequestHandler implements RequestHandler {

    private final UserService userService;

    @Override
    public Optional<Message> handle(Message msg) {
        UserDto userDto = Serializers.deserialize(msg.getPayload(), UserDto.class);
        long id = userService.createUser(userDto);

        return Optional.of(
                Message.builder()
                        .from(msg.getTo())
                        .to(msg.getFrom())
                        .payload(Serializers.serialize(id))
                        .sourceMessageId(msg.getId())
                        .type(MessageType.CREATE_USERS_DATA.getValue()).build()
        );
    }
}
