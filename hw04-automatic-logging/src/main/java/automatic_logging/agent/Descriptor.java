package automatic_logging.agent;

public enum Descriptor {

    STRING_D("Ljava/lang/String;"),
    INT_D("I");

    private String value;


    Descriptor(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
