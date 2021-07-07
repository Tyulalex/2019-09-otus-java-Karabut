package message.system.frontendservice;

import message.system.dto.UserDto;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {

    void getUsersData(Consumer<String> dataConsumer);

    void createUser(UserDto userDto, Consumer<String> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}
