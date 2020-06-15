package message.system.dto;

import lombok.Data;

@Data
public class MessageDto {

    private MessageType messageType;

    private String payload;
}
