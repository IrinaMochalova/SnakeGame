package proto.Interfaces;

public interface IClient <T> {
    char[] receive();
    void send(char[] message);

    boolean hasMessage();
    boolean isConnected();

    T getInterface();
}
