package proto.Interfaces;

public interface IClientListener<T extends IClient> {
    boolean hasClient();
    T accept();

    void close();
}
