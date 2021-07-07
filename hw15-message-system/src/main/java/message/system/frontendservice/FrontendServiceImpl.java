package message.system.frontendservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.system.dto.UserDto;
import message.system.messagesystem.Message;
import message.system.messagesystem.MessageType;
import message.system.messagesystem.MsClient;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class FrontendServiceImpl implements FrontendService {

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final String databaseServiceClientName;

    @Override
    public void getUsersData(Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, null, MessageType.GET_USERS_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void createUser(UserDto userDto, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, userDto, MessageType.CREATE_USERS_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            log.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }
}
