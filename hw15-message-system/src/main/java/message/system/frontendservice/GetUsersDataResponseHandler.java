package message.system.frontendservice;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.system.messagesystem.Message;
import message.system.messagesystem.RequestHandler;
import message.system.utils.Serializers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetUsersDataResponseHandler implements RequestHandler {

    private final FrontendService frontendService;
    private final Gson gson;

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            String userData = gson.toJson(Serializers.deserialize(msg.getPayload(), List.class));
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(userData));

        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
