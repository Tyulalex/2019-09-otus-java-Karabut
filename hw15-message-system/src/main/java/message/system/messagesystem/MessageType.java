package message.system.messagesystem;

public enum MessageType {

    GET_USERS_DATA("UsersData"),

    CREATE_USERS_DATA("CreateUsers");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
