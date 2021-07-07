package message.system.messagesystem;

import lombok.Getter;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Message {

    static final Message VOID_MESSAGE = new Message();

    private final UUID id = UUID.randomUUID();

    private final String from;

    private final String to;

    private final String type;

    private final byte[] payload;

    private final UUID sourceMessageId;


    private Message() {
        this.from = null;
        this.to = null;
        this.type = "voidTechnicalMessage";
        this.payload = new byte[1];
        this.sourceMessageId = null;
    }

    private Message(String from, String to, String type, byte[] payload, UUID sourceMessageId) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.payload = payload;
        this.sourceMessageId = sourceMessageId;

    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Optional<UUID> getSourceMessageId() {
        return Optional.ofNullable(sourceMessageId);
    }


    public static class MessageBuilder {
        private String from;
        private String to;
        private String type;
        private byte[] payload;
        private UUID sourceMessageId;

        MessageBuilder() {
        }

        public MessageBuilder from(String from) {
            this.from = from;
            return this;
        }

        public MessageBuilder to(String to) {
            this.to = to;
            return this;
        }

        public MessageBuilder type(String type) {
            this.type = type;
            return this;
        }

        public MessageBuilder payload(byte[] payload) {
            this.payload = payload;
            return this;
        }

        public MessageBuilder sourceMessageId(UUID sourceMessageId) {
            this.sourceMessageId = sourceMessageId;
            return this;
        }

        public Message build() {
            return new Message(from, to, type, payload, sourceMessageId);
        }

        public String toString() {
            return "Message.MessageBuilder(from=" + this.from + ", to=" + this.to + ", type=" + this.type + ", payload=" + java.util.Arrays.toString(this.payload) + ", sourceMessageId=" + this.sourceMessageId + ")";
        }
    }
}
