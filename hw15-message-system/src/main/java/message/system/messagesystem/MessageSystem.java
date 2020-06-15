package message.system.messagesystem;

public interface MessageSystem {

    void addClient(MsClient msClient);

    boolean newMessage(Message message);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}
