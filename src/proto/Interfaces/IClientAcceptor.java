package proto.Interfaces;

public interface IClientAcceptor<T extends IClient> {
    boolean hasClient();
    T accept();
}
